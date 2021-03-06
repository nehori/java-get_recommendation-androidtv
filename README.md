Sample source code to get Notification on Recommendation Area on AndroidTV.
=============

## Introduction
NotificationListenerService allows an third-party application to receive notification information.
More information please contact ([developer.android.com][1]).

## ScreenShot

![Image](https://raw.githubusercontent.com/nehori/java-get_recommendation-androidtv/master/screenshot/3.png)

## Note

This application requires android.permission.WRITE_SECURE_SETTINGS.
But Android TV does not provide any settings like "Settings > Sound & notification > Notification access".
You need to install the built apk as Android System Apps.

## License
Copyright 2016 nehori.

Licensed under the Apache License, Version 2.0 (the "License");
You may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

[1]: http://developer.android.com/reference/android/service/notification/NotificationListenerService.html
