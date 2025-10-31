# Gluky-Clients

**v1.0.2**

This repository contains the client versions of **Gluky**,
so if you want to customize you can fork it and work on it, if there are any errors, fixes to do or
some idea to upgrade this project, please open a ticket or contact us to talk about, thanks and good
use!

This is an open source self-hosted project useful to daily track the glucose levels and meals.

Gluky is an open-source, self-hosted project designed to help users track their glucose levels and meals on a daily basis. It allows users to log measurements associated with specific meals—such as breakfast, morning snack, lunch, afternoon snack, and dinner. Gluky also supports the creation of customizable reports over different time periods, including one week, one month, three months, or four months. Following a report example:

<img style="max-width:100%; height:auto;" src="images/report_demo.png" alt="report demo"/>

> [!NOTE]  
> All the data have been random generated and are not related to anyone, for more information look
> to the [dataset](https://github.com/N7ghtm4r3/Gluky/blob/main/dataset/README.md)

## Architecture

At the moment, **Gluky** is available as a self-hosted product, but I'm working to take it one step
further — turning it into a fully served solution for those who want to use it right away, with no
setup required.

The goal is to offer **Gluky both as a hosted service and as an open source project** — so users
will always be free to self-host, contribute, and customize it as they wish.

To make this possible, I'm looking for a small amount of support to cover a few essential costs:

- A [license for the Apple Developer Program](https://developer.apple.com/programs/), so I can
  publish Gluky on the **App Store**.
- Hosting for the **WebApp**, with a dedicated domain.
- A **main server infrastructure**, ready to serve users out of the box.

If you believe in this project and want to help make Gluky more accessible to everyone, your
contribution would mean a lot. The open source core will remain free and community-driven — the
hosted version is simply an option for convenience!

Feel free to [contribute](#donations)!

### Clients

- [Android](https://play.google.com/store/apps/details?id=com.tecknobit.gluky)
- [Gluky desktop version](https://github.com/N7ghtm4r3/Gluky-Clients/releases/tag/1.0.2)
- iOS -> source code available, but cannot distribute due
  missing [Apple Developer Program license](https://developer.apple.com/programs/)
- [Gluky webapp version](https://github.com/N7ghtm4r3/Gluky-WebApp)

### Backend

- [Backend service "out-of-the-box"](https://github.com/N7ghtm4r3/Gluky/releases/tag/1.0.1)

## Usages

### Run your own backend instance

See how to run your own **Gluky** backend instance service reading
the [Gluky backend procedures](https://github.com/N7ghtm4r3/Gluky#readme)

## Customize the application

To customize and create your own version of this application you need to have
the [core library](https://github.com/N7ghtm4r3/Gluky/tree/main/core)
implemented in your project and published into maven local system

### Clone the core library and publish to maven local

- Clone the repository or download the zip file of the current version available

- Open the folder file in your development environment and publish to maven local with the
  **publishMavenPublicationToMavenLocal** gradle task, take a
  look [here](https://docs.gradle.org/current/userguide/publishing_maven.html)
  for a help

### Implement the core library to your application

- #### Gradle (Short)

```gradle
repositories {
  ...
  mavenLocal()
}

dependencies {
  implementation 'com.tecknobit.glukycore:glukycore:1.0.0'
}
```

#### Gradle (Kotlin)

```gradle
repositories {
  ...
  mavenLocal()
}

dependencies {
  implementation("com.tecknobit.glukycore:glukycore:1.0.0")
}
```

## Desktop appearance

<details>
  <summary>Desktop UI</summary>
  <img src="https://github.com/N7ghtm4r3/Gluky-Clients/blob/main/images/measurements_desktop.png" alt="measurements"/>
  <img src="https://github.com/N7ghtm4r3/Gluky-Clients/blob/main/images/analyses_desktop.png" alt="analyses"/>
</details>

## Illustrations

Part of the illustrations were sourced from [undraw](https://undraw.co) and from
[storyset](https://storyset.com), in particular:

- [Food illustrations by Storyset](https://storyset.com/food)
- [Nature illustrations by Storyset](https://storyset.com/nature)

## Support

If you need help using the library or encounter any problems or bugs, please contact us via the
following links:

- Support via [email](mailto:infotecknobitcompany@gmail.com)
- Support via [GitHub](https://github.com/N7ghtm4r3/Gluky-Clients/issues/new)

Thank you for your help!

## Badges

[![](https://img.shields.io/badge/Google_Play-414141?style=for-the-badge&logo=google-play&logoColor=white)](https://play.google.com/store/apps/developer?id=Tecknobit)
[![Twitter](https://img.shields.io/badge/Twitter-1DA1F2?style=for-the-badge&logo=twitter&logoColor=white)](https://twitter.com/tecknobit)

[![](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)](https://www.oracle.com/java/)
[![](https://img.shields.io/badge/Kotlin-0095D5?&style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://play.google.com/store/apps/details?id=com.tecknobit.gluky)

## Donations

If you want support project and developer

| Crypto                                                                                              | Address                                          | Network  |
|-----------------------------------------------------------------------------------------------------|--------------------------------------------------|----------|
| ![](https://img.shields.io/badge/Bitcoin-000000?style=for-the-badge&logo=bitcoin&logoColor=white)   | **3H3jyCzcRmnxroHthuXh22GXXSmizin2yp**           | Bitcoin  |
| ![](https://img.shields.io/badge/Ethereum-3C3C3D?style=for-the-badge&logo=Ethereum&logoColor=white) | **0x1b45bc41efeb3ed655b078f95086f25fc83345c4**   | Ethereum |
| ![](https://img.shields.io/badge/Solana-000?style=for-the-badge&logo=Solana&logoColor=9945FF)       | **AtPjUnxYFHw3a6Si9HinQtyPTqsdbfdKX3dJ1xiDjbrL** | Solana   |

If you want support project and developer
with [PayPal](https://www.paypal.com/donate/?hosted_button_id=5QMN5UQH7LDT4)

## Privacy policy

This section outlines the privacy practices and policies for the use of the Gluky application,
available on the Google Play Store.

The Gluky application may anonymously collect and send performance reports, as well as track any
issues or errors encountered during your experience. The data collected includes information related
to the device, such
as the brand, model, operating system, and operating system version, but only when an issue occurs.
In the case of a
web-based issue, the application may also collect the web user agent, browser, and browser version.

This information is used solely to improve the application's functionality and user experience,
ensuring a seamless and
efficient service.

Copyright © 2025 Tecknobit
