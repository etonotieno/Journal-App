/*
 * Copyright (C) 2018 Eton Otieno Oboch
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.edoubletech.journalapp.injection;

import com.edoubletech.journalapp.ui.calendar.CalendarFragment;
import com.edoubletech.journalapp.ui.LoginActivity;
import com.edoubletech.journalapp.ui.NavHostActivity;
import com.edoubletech.journalapp.ui.add.AddFragment;
import com.edoubletech.journalapp.ui.calendar.SelectedFragment;
import com.edoubletech.journalapp.ui.main.MainFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    void inject(AddFragment addFragment);

    void inject(MainFragment mainFragment);

    void inject(SelectedFragment selectedFragment);

    void inject(CalendarFragment fragment);

    void inject(NavHostActivity navHostActivity);

    void inject(LoginActivity loginActivity);
}
