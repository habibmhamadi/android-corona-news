package com.habib.coronanews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.os.Handler;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.habib.coronanews.Fragments.Countries;
import com.habib.coronanews.Fragments.NewsList;
import com.habib.coronanews.Fragments.Overall;

public class HomeActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private BottomNavigationView bottomNavigationView;
    private Fragment overall,countries,news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
        fragmentManager.beginTransaction().add(R.id.frameHomeContainer,overall, Overall.class.getSimpleName()).commit();
    }

    private void init(){
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        fragmentManager = getSupportFragmentManager();
        overall = new Overall();
        countries = new Countries();
        news = new NewsList();
        bottomNavigationView.setOnNavigationItemSelectedListener(v->{
            switch (v.getItemId()){
                case R.id.home:{
                    Fragment countries = fragmentManager.findFragmentByTag(Countries.class.getSimpleName());
                    Fragment news = fragmentManager.findFragmentByTag(NewsList.class.getSimpleName());
                    if (countries!=null ){
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag(Countries.class.getSimpleName())).commit();
                    }
                    if (news!=null ){
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag(NewsList.class.getSimpleName())).commit();
                    }

                    fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag(Overall.class.getSimpleName())).commit();

                    break;
                }
                case R.id.list:{
                    fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag(Overall.class.getSimpleName())).commit();
                    Fragment news = fragmentManager.findFragmentByTag(NewsList.class.getSimpleName());
                    Fragment countries = fragmentManager.findFragmentByTag(Countries.class.getSimpleName());
                    if (news!=null){
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag(NewsList.class.getSimpleName())).commit();
                    }
                    if (countries!=null) {
                        fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag(Countries.class.getSimpleName())).commit();
                    }else{
                        fragmentManager.beginTransaction().add(R.id.frameHomeContainer,new Countries(),Countries.class.getSimpleName()).commit();
                    }
                    break;
                }
                case R.id.news:{
                    fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag(Overall.class.getSimpleName())).commit();
                    Fragment news = fragmentManager.findFragmentByTag(NewsList.class.getSimpleName());
                    Fragment countries = fragmentManager.findFragmentByTag(Countries.class.getSimpleName());
                    if (countries!=null){
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag(Countries.class.getSimpleName())).commit();
                    }
                    if (news!=null) {
                        fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag(NewsList.class.getSimpleName())).commit();
                    }else{
                        fragmentManager.beginTransaction().add(R.id.frameHomeContainer,new NewsList(), NewsList.class.getSimpleName()).commit();
                    }
                    break;
                }
            }
            return true;
        });
    }

    int timer = 0;
    @Override
    public void onBackPressed() {
        if (timer==1){
            super.onBackPressed();
        }else{
            timer++;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    timer=0;
                }
            },1200);
        }
    }
}
