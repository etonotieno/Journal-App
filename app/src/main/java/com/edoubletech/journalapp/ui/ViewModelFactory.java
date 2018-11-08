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

package com.edoubletech.journalapp.ui;

import com.edoubletech.journalapp.data.MainRepository;
import com.edoubletech.journalapp.ui.add.AddViewModel;
import com.edoubletech.journalapp.ui.main.MainViewModel;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private MainRepository mRepo;

    @Inject
    public ViewModelFactory(MainRepository repository) {
        mRepo = repository;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked cast")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class))
            return (T) new MainViewModel(mRepo);
        else if (modelClass.isAssignableFrom(AddViewModel.class))
            return (T) new AddViewModel(mRepo);
        else if (modelClass.isAssignableFrom(BaseViewModel.class))
            return (T) new BaseViewModel(mRepo);
        else {
            throw new IllegalArgumentException(modelClass.getName() + "Not found.");
        }
    }
}
