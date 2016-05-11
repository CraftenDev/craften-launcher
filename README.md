# Craften Launcher [![Build Status](https://ci.wertarbyte.com/job/CraftenLauncher/badge/icon)](https://ci.wertarbyte.com/job/CraftenLauncher/)

Craften Launcher is a beautiful and open-source alternative for the official
Minecraft launcher. It supports all Minecraft versions and also works nicely
with modded installations.

## Command line usage

While you could just start the launcher and enter your credentials manually,
it also supports the following command line arguments.

| Argument            | Description                                            |
|---------------------|--------------------------------------------------------|
| `-mcpath <path>`    | Specify a custom Minecraft path.                       |
| `-server <address>` | Join a server after launching.                         |
| `-xmx <memory>`     | Specify maximum memory Minecraft will be started with. |
| `-profileid <id>`   | Automatically selects the user by their profile id.    |
| `-quickplay`        | Immediately launches the previosly selected (or latest) version, if the user is still logged in.               |
| `-forcelogin`       | Disables automatic login.                              |
| `-fullscreen`       | Launches Minecraft in fullscreen mode.                 |

Example usage: `java -jar CraftenLauncher.jar -server play.craften.de -fullscreen` :wink:

## License
Craften Launcher is licensed under the MIT license, see the
[license file][license].

[license]: https://github.com/TeamWertarbyte/craften-launcher/blob/master/LICENSE
