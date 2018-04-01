# :rotating_light: Auther

[![releases][releases]][releases-url]
[![jitpack][jitpack]][jitpack-url]
[![tests][tests]][tests-url]
[![license][license]][license-url]

A flexible BungeeCord client authentication API.

## Features

* Flexible API to determine whether a player needs to be authenticated with Mojang.
* Follows the [protocol](http://wiki.vg/Protocol#Login_Start), doesn't use "hacks".
* Safe by default (all the clients need to be authenticated unless told otherwise)

## Getting started

Install Prose using [`maven`](https://maven.apache.org/) by adding the JitPack repository to your `pom.xml` file:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Next, add the `auther-api` dependency:

```xml
<dependency>
    <groupId>com.github.hugmanrique.Auther</groupId>
    <artifactId>auther-api</artifactId>
    <version>master-SNAPSHOT</version>
</dependency>
```

You will need to have Java 8 version 45 or later (older versions _might_ work).

Let's get started by getting the `Auther` instance:

```java
private Auther getAuther() {
    try {
      return (Auther) getProxy().getPluginManager().getPlugin("Auther");
    } catch (ClassCastException e) {
      throw new RuntimeException("Could not find Auther plugin", e);
    }
}
```

Next, create an `AuthDeterminer`, the class that will choose whether a player needs to authenticate or not:

```java
public class DummyDeterminer implements AuthDeterminer {
    @Override
    public boolean shouldAuthenticate(PendingConnection connection) {
        // Skip authentication if name is "AwesomePlayer"
        return !connection.getName().equals("AwesomePlayer");
    }
}
```

Now, register the determiner:

```java
private void registerDeterminer(Auther auther) {
    auther.setDeterminer(new DummyDeterminer());
}
```

Finally, you will need to add `"Auther"` as a plugin dependency to your `plugin.yml` file. Your plugin users will need to install Auther on their servers, but thankfully we provide compiled artifacts on the [Releases](https://github.com/hugmanrique/Auther/releases) page.

> You will need to set `online-mode` to `true` on your BungeeCord's `config.yml` file to auth with Mojang by default. This plugin won't work on "cracked" proxies.

## Examples

### Disable authentication for known players and IPs if Mojang servers go down

You can make an HTTP request to `https://status.mojang.com/check` to see if the auth servers are down. If they are, you can still let known player and address keypairs join because they played on your server before:

```java
public class AuthDownDeterminer implements AuthDeterminer {
    @Override
    public boolean shouldAuthenticate(PendingConnection connection) {
        if (!isMojangDown()) {
            // Always auth if Mojang is up
            return true;
        }

        // No need to auth if seen before
        return !authenticatedBefore(connection.getName(), connection.getAddress());
    }

    private boolean isMojangDown() {
        // TODO Ping the API and cache response
        return true;
    }

    private boolean authenticatedBefore(String playerName, InetSocketAddress address) {
        // TODO Lookup player and IP address on the database
        return true;
    }
}
```

### Auth players who enabled "no /login mode" on your server

```java
public class NoLoginDeterminer implements AuthDeterminer {
    private final PostLoginListener loginListener;

    public NoLoginDeterminer(PostLoginListener loginListener) {
        this.loginListener = loginListener;
    }

    @Override
    public boolean shouldAuthenticate(PendingConnection connection) {
        String playerName = connection.getName();
        boolean noLogin = hasEnabledNoLoginMode(playerName);

        if (noLogin) {
            // Will add the player's name to a set
            // that contains players that can be moved
            // to the Hub without using /login on join
            loginListener.addToNoLoginQueue(playerName);
        }

        // Needs to auth with Mojang
        return noLogin;
    }

    private boolean hasEnabledNoLoginMode(String playerName) {
        // TODO Lookup player on the database
        return false;
    }
}
```

## License

[MIT](LICENSE) &copy; [Hugo Manrique](https://hugmanrique.me)

[releases]: https://img.shields.io/github/downloads/hugmanrique/Auther/total.svg
[releases-url]: https://github.com/hugmanrique/Auther/releases
[jitpack]: https://jitpack.io/v/hugmanrique/Auther.svg
[jitpack-url]: https://jitpack.io/#hugmanrique/Auther
[tests]: https://img.shields.io/travis/hugmanrique/Auther/master.svg
[tests-url]: https://travis-ci.org/hugmanrique/Auther
[license]: https://img.shields.io/github/license/hugmanrique/Auther.svg
[license-url]: LICENSE
