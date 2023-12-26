package kr.jeet.edu.youtubetest;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class MainActivity extends AppCompatActivity {

    Button btnShowAds;
    TextView txtDisplayMess;

    private static String ads_id= "ca-app-pub-3940256099942544/5224354917";
    private RewardedAd mRewardedAd;
    private String TAG = "MY_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnShowAds= findViewById(R.id.Ad_Button);
        txtDisplayMess = findViewById(R.id.tv_ad);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                loadAdsRequest();
            }
        });

        btnShowAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Show Ads",Toast.LENGTH_SHORT).show();
                if(mRewardedAd != null){

                    mRewardedAd.show(MainActivity.this, new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            //Handle the reward.
                            Log.d(TAG, "The user earned the reward.");
                            int rewardAmount = rewardItem.getAmount();
                            String rewardType = rewardItem.getType();

                            txtDisplayMess.setText(rewardAmount +" : "+ rewardType);

                        }
                    });

                }else{
                    Log.d(TAG,"The rewarded ad wasn't ready yet.");
                }
            }
        });



    }

    private void loadAdsRequest() {
        AdRequest adRequest = new AdRequest.Builder().build();

        RewardedAd.load(this, ads_id,
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        //Handle the error.
                        Log.d(TAG, loadAdError.getMessage());
                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        Log.d(TAG,"Ad was loaded.");
                        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                                Log.d(TAG,"Ad was shown.");
                                mRewardedAd = null;
                            }


                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when ad fails to show.
                                Log.d(TAG,"Ad failed to show.");
                            }


                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when ad is dismissed.
                                //Don't forget to set the ad reference to null so you
                                // don't show the ad a second time.
                                Log.d(TAG,"Ad was dismissed.");
                                loadAdsRequest();
                            }
                        });
                    }
                });
    }


}