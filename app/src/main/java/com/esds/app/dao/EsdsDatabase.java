package com.esds.app.dao;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.esds.app.model.Cart;

@Database(entities = {Cart.class}, version = 1)
public abstract class EsdsDatabase extends RoomDatabase{

    public abstract CartDao cartDao();
}
