# Example Prefab Dropwizard Application

This repo shows how to add [Prefab] to your Dropwizard app to get access to features like

- Dynamic log levels
- Feature flags
- Live config

Follow along on YouTube (coming soon)

## Dropwizard Archetype

The [Dropwizard skeleton commit](https://github.com/prefab-cloud/example-dropwizard-app/commit/d5fefbd7d596b2eaee7130b04005ee221f09ea00) was built using `mvn archetype:generate -DarchetypeGroupId=io.dropwizard.archetypes -DarchetypeArtifactId=java-simple -DarchetypeVersion=2.1.6`.

## Initial Content

[Full diff](https://github.com/prefab-cloud/example-dropwizard-app/compare/new-repo...initial-content)

Before adding Prefab to the project, We'll add a very basic one page site that will allow logging in as various users. Nothing Prefab specific here, so just a quick overview.

* [CustomAuthProvider](https://github.com/prefab-cloud/example-dropwizard-app/blob/initial-content/src/main/java/com/example/auth/CustomAuthFilter.java) Implements username only "login" process to set a current user in the session, and if there isn't a user, set it to be Jeff, our CEO.
* [HomeController](https://github.com/prefab-cloud/example-dropwizard-app/blob/initial-content/src/main/java/com/example/resources/HomeResource.java) to build out a template that will be rendered by our mustache view
* [Home View](https://github.com/prefab-cloud/example-dropwizard-app/blob/initial-content/src/main/resources/com/example/views/home.mustache) to render our home page with its library of users to log in as.
* Adds stock images for our fake users.

We also add the dropwizard-guice plugin to use guice instead of HK2 dependency injection.

Now you can start the app with `mvn clean package &&  java -jar target/example-dropwizard-1.0-SNAPSHOT.jar server config.yml`

## Install Prefab

[Full diff](https://github.com/prefab-cloud/example-dropwizard-app/compare/initial-content...install-prefab)
### Dependencies

First we need to add maven dependencies for Prefab by adding a property to control the version

```xml
<prefab.version>0.3.8</prefab.version>
```
Then our dependencies
```xml
<dependency>
    <groupId>cloud.prefab</groupId>
    <artifactId>client</artifactId>
    <version>${prefab.version}</version>
</dependency>
<dependency>
    <groupId>cloud.prefab</groupId>
    <artifactId>logback-listener</artifactId>
    <version>${prefab.version}</version>
</dependency>
```

### Dependency Injection

We'll next write a Guice module class to handle initializing instances of the prefab clients and making them available for injection as-needed.

[PrefabModule](https://github.com/prefab-cloud/example-dropwizard-app/blob/install-prefab/src/main/java/com/example/config/PrefabModule.java) looks like this

```java
public class PrefabModule extends AbstractModule {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrefabModule.class);

    protected void configure() {
        // do configuration
    }

    @Provides
    @Singleton
    PrefabCloudClient getPrefabCloudClient(){
        return new PrefabCloudClient(new Options());
    }
    @Singleton
    @Provides
    ConfigClient getConfigClient(PrefabCloudClient prefabCloudClient){
        ConfigClient configClient = new ConfigClientImpl(
                prefabCloudClient
        );
        LOGGER.info("Installing PrefabContextTurboFilter");
        PrefabContextTurboFilter.install(configClient);
        return configClient;
    }

    @Singleton
    @Provides FeatureFlagClient featureFlagClient(PrefabCloudClient prefabCloudClient) {
        return prefabCloudClient.featureFlagClient();
    }

}
```

The `getPrefabCloudClient` method creates a Prefab Options object and instantiates a PrefabCloudClient with those options

### Get a Prefab API Key
The last part of adding Prefab to your app is to get an API key from https://app.prefab.cloud and set it as the environment variable PREFAB_API_KEY. We'll restart our app to make sure it uses that env var.

That's all it takes to add Prefab to your app. Now let's take some of the features for a test drive.

## Dynamic log levels

Add these log lines to the HomeController

```java
LOG.debug("🔍 Hello debug logger");
LOG.info("ℹ️ Hello info logger");
LOG.warn("⚠️ Hello warn logger");
LOG.error("🚨 Hello error logger");
```

The default log level in the [config.yml](https://github.com/prefab-cloud/example-dropwizard-app/config.yml) file is INFO. When we visit http://localhost:8080 we should see the ERROR, WARN output but not the INFO or DEBUG.

In the Prefab UI, let's set our "Root Log Level" to "WARN". Now reloading http://localhost:8080 shows the ERROR, WARN output. Note how the output changes.

Prefab lets you change log levels on the fly. We can even set log levels for specific packages and classes by fully qualified class name.

Once your app has been running for about a minute it will have phoned-home stats about the logging output to prefil the log-level UI in prefab.

## Configure Prefab Contexts for targeted Log Levels

We can get even more specific about when to log by providing the Prefab client with more contextual information about what your app is doing for whom. Let's check it out.

[Full diff](https://github.com/prefab-cloud/example-dropwizard-app/compare/install-prefab...configure-prefab-context)

The default `ContextStore`, `ThreadLocalContextStore` uses a thread local to keep state for the life of the request. To support that we'll want two filters, one to initialize some global context based on the logged in user, and another filter after the response is rendered to clear the threadlocal so that thread is clean for reuse.

First we add a [ContainerRequestFilter](https://github.com/prefab-cloud/example-dropwizard-app/blob/main/src/main/java/com/example/prefab/PrefabContextAddingRequestFilter.java) to add a prefab context based on the currently "logged in' user.

```java
      public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        // we're not using real auth here so we won't be using eg `containerRequestContext.getSecurityContext()`
        CustomAuthFilter.getUserFromSession(requestProvider.get().getSession()).ifPresent(user -> {
        LOGGER.info("Adding context for {}", user.name());
        configClient.getContextStore().addContext(PrefabContext.newBuilder("User")
            .put("name", user.name())
            .put("id", user.id())
            .put("email", user.email())
            .put("country", user.country())
            .build());
        });
        }
```

Nex we add a [ContainerResponseFilter](https://github.com/prefab-cloud/example-dropwizard-app/blob/main/src/main/java/com/example/prefab/PrefabContexClearingResponseFilter.java) to call `clearContext()`

Lets restart the app and try it out!

Now imagine Jeff has reported a problem, so lets see how we can get more logs for him. Let's go back to the log configuration in Prefab add a targetted option for user id on the HomeController log levels at debug for id=1, and set warn as the default. Reload the page as Jeff and we can see all the logs, but for anyone else the logs are much quieter.

## Feature Flags

[Full diff](https://github.com/prefab-cloud/example-dropwizard-app/compare/configure-prefab-context...add-feature-flag)

Let's use the information we put in the context to disable the cookie banner for some users. Lets create a feature flag called "gdpr.banner" with a default value of true then use it in our application.

After we inject `FeatureFlagClient` into the `HomeController` we can evaluate the flag and place the result into the template like this.

```java
 .setShowGdprBanner(featureFlagClient.featureIsOn("gdpr.banner"));
```

Note that we don't pass a context in, because we've already set a request-scoped property containing the user data.

in the template we'll wrap the banner in a block like this
```
{{#showGdprBanner}}
[the banner]
{{/showGdprBanner}}
```

Restart the app and we can see the banner is still there. Now we'll update the feature flag's rules to evaluate to false for users with "country' equal to "US" . Logging in as Jeff and the banner is gone, logging in as someone else and the banner is back.


## Coming Soon: All the Configs

In our [micronaut example](https://github.com/prefab-cloud/example-micronaut-app) I added a table to show the default and context-sensitive rendered config values - that's not yet done here but will be done presently.


[Prefab]: https://prefab.cloud
[Sign up]: https://app.prefab.cloud/users/sign_up
[dynamic log levels]: https://docs.prefab.cloud/docs/java-sdk/java-logging
