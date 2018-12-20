package com.killain.organizer.packages.interfaces;

import com.killain.organizer.packages.interactors.DBInjectorModule;
import com.killain.organizer.packages.interactors.DataManager;

import dagger.Component;

@Component (modules = DBInjectorModule.class)
public interface DBComponent {
    void injectInManager(DataManager dataManager);
}
