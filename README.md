# Isybank

**Isybank** is a Spigot plugin designed for Minecraft server version **1.21.1** and coded in **Java 21**. This plugin adds economy features to your Minecraft server, helping manage server's economy.

## Features

- Bank system for players to deposit and withdraw money.
- Integrated with VaultAPI to support any economy plugin compatible with Vault.
- SQL storage for persistent bank data.

## Requirements

- **Minecraft Version**: 1.21.1
- **Java Version**: 21
- **VaultAPI**: Required for economy functionality
- **Spigot Server**: Ensure your server is running Spigot 1.21.1 or higher.

## Installation

1. Download the latest version of **Isybank**.
2. Place the plugin `.jar` file into your server's `plugins` folder.
3. Restart or reload your server to load the plugin.
4. Make sure you have VaultAPI and an economy plugin installed and configured on your server.

## Commands

```mclang
(argument) = optional
<argument> = mandatory
```

### User commands:
```mclang
/balance (player), it requires isybank.user
/pay <player> <amount> # Sends money <player> the <amount> specified, it requires isybank.user
```

### Admin commands:
```mclang
/money <player> # Same as /balance, it requires isybank.admin
/money <player> <give/remove/set> <amount> # Gives, Removes or Sets the amount from/for/to <player>, it requires isybank.admin
```
## Permissions

- `isybank.user`: Allows players to use the basic commands.
- `isybank.admin`: Grants access to administrative commands.

## Configuration

The plugin comes with a configuration file where you can adjust after the first run, check the `plugins/Isybank/config.yml` file for customization options.

## Contributing

Contributions are welcome! Please fork the repository and submit a pull request with your changes. Make sure your code includes proper documentation.

## License

**Isybank** is licensed under the **GPL-3.0 License**. You are free to modify and distribute the plugin under the terms of this license.

For more information, see the [LICENSE](./LICENSE) file.

## Credits

References:
 - [AlessioDP - Libby](https://github.com/AlessioDP/libby)
 - [brettwooldridge - HikariCP](https://github.com/brettwooldridge/HikariCP)
 - [Ronmamo - Reflections](https://github.com/ronmamo/reflections)
 - [Milkbowl - VaultAPI](https://github.com/MilkBowl/VaultAPI)

Plugin created by Sodiograaz - Gabriel