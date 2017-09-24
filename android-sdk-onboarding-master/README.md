# android-sdk-onboarding
Example app for Android SDK Onboarding flow

HyperTrack Onboarding link: https://dashboard.hypertrack.com/onboarding/sdk/android

## Install the SDK

1. In your app's `build.gradle` file, define the minimum SDK version, repositories and dependencies as shown. Once configured, run a gradle sync to import the SDK and its dependencies to your project.

![Gradle file](https://s3.amazonaws.com/dashboard-v3-assets/gradle.png)

2. Once Gradle sync is complete, you can use the SDK methods inside your project. Configure your publishable key and initialise the SDK in the `onCreate` method of your Application class. This needs to be done only once in the app lifecycle.

> **Account keys**
> Sign up to get account keys if you haven't already.

# Enable location
The SDK uses the `ACCESS_FINE_LOCATION` and `ACCESS_COARSE_LOCATION` permissions, which are already included in the SDK manifest file.

## Run-time permissions
* `requestPermissions()` API: to enable location permissions on run-time
* `requestLocationServices()` API: to enable location services at high accuracy

# Identify device
The SDK needs a **User** object to identify the device. The SDK has a convenience method `getOrCreateUser()` to lookup an existing user using a unique identifier (called `lookupId`) or create one if necessary.

Method parameters

| Parameter | Description |
|-----------|-------------|
| userName  | Name of the user entity |
| phone     | Phone number of the user entity |
| lookupId  | Unique identifier for your user |

Use this API in conjunction to your app's login flow, and call `getOrCreate` at the end of a successful login flow. This API is a network call, and needs to be done only once in the user session lifecycle.

# Start tracking
Use the `startTracking()` method to start tracking. Once the user starts tracking, you can see **Trips** and **Stops** of the user.

This is a non-blocking API call, and will also work when the device is offline. 

> **View on the dashboard**
> View the user's trips and stops [here](https://dashboard.hypertrack.com).

# Stop tracking
Use the `stopTracking()` method to stop tracking. This can be done when the user logs out.

# Simulate a trip for Testing
To simulate a trip, replace `startTracking` API with `startMockTracking` and `stopTracking` API with `stopMockTracking`.
Refer to our [docs](docs.hypertrack.com/sdks/android/basic.html#simulate-a-trip-for-testing) for more info.

> **Ready to deploy!**
> Your Android app is all set to be deployed on the Play Store. As your users update and log in, their live location will be visualized on this dashboard.

## Next steps
* Add team members to the HyperTrack dashboard
* Follow one of our use-case tutorials to build your live location feature
