package com.example.weather;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MyPageAdapter extends FragmentStateAdapter {
    public MyPageAdapter(@NonNull FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position==0){
            return new NorthChina();
        }else if(position==1){
            return new NorthEast();
        }else if(position==2){
            return new SouthChina();
        }else if(position==3){
            return new NorthWest();
        }else if(position==4){
            return new SouthWest();
        }else if(position==5){
            return new EastChina();
        }else{
            return new CentralChina();
        }
    }

    @Override
    public int getItemCount() {
        return 7;
    }

}
