package com.example.weipeixian.MYYDBG.ui.activity.process;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import com.avos.avoscloud.AVObject;
import com.example.weipeixian.MYYDBG.R;
import androidx.navigation.fragment.NavHostFragment;

public class add_process extends AppCompatActivity {
    private AVObject avObject =new AVObject("Process");
    public AVObject getAVObject() {
        return avObject;
    }
    public void setAVObject(AVObject title) {
        this.avObject = title;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_process);
    }
    @Override
    public boolean onSupportNavigateUp() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.my_nav_host_fragment);
        return NavHostFragment.findNavController(fragment).navigateUp();
    }
}
