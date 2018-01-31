package com.esds.app.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.esds.app.model.Cart;

@Dao
public interface CartDao {

    @Query("select * from cart where visit_id = :visitId")
    public Cart[] select(int visitId);

    @Insert
    public void insert(Cart cart);

    @Update
    public void update(Cart cart);

    @Delete
    public void delete(Cart cart);

    @Query("delete from cart where visit_id = :visitId")
    public void truncate(int visitId);

}
