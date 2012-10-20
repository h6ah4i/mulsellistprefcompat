MultiSelectListPreferenceCompat
====================

Backport MultiSelectListPreference class to older android devices.

Originally MultiSelectListPreference was introduced to API level 11 (Honeycomb).
And official support package does not include this class. 
This library introduces compatible class of MultiSelectListPreference.


Usage
-
Simply replace MultiSelectListPreference to com.h6ah4i.android.compat.preference.MultiSelectListPreferenceCopmat:

    <MultiSelectListPreference ~~~ />

to 

    <com.h6ah4i.android.compat.preference.MultiSelectListPreferenceCompat ~~~ />    


License
-

   Copyright 2012 Haruki Hasegawa (h6ah4i)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

