package com.mrprona.dota2assitant.hero.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.mrprona.dota2assitant.R;
import com.mrprona.dota2assitant.base.util.SteamUtils;
import com.mrprona.dota2assitant.base.util.Utils;
import com.mrprona.dota2assitant.hero.api.Hero;
import com.util.responses.HeroInfo;
import com.util.responses.Role5Info;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * User: ABadretdinov
 * Date: 15.01.14
 * Time: 14:43
 */
public class HeroStatInfo extends Fragment {

    private Hero mHero;
    private ImageView mImageView;


    @BindView(R.id.progress_easeofuse)
    RoundCornerProgressBar progressEasyOfUse;

    @BindView(R.id.progress_range)
    RoundCornerProgressBar progressRange;

    @BindView(R.id.progress_accuracy)
    RoundCornerProgressBar progressAccuracy;

    @BindView(R.id.progress_power)
    RoundCornerProgressBar progressPower;

    @BindView(R.id.progress_mobility)
    RoundCornerProgressBar progressMobility;

    @BindView(R.id.progress_stamina)
    RoundCornerProgressBar progressStamina;

    @BindView(R.id.progress_utility)
    RoundCornerProgressBar progressUtility;

    @BindView(R.id.progress_crowd_control)
    RoundCornerProgressBar progressCrowdControl;


    @BindView(R.id.imgNameStar1)
    ImageView imgNameStar1;

    @BindView(R.id.imgNameStar2)
    ImageView imgNameStar2;

    @BindView(R.id.imgNameStar3)
    ImageView imgNameStar3;

    @BindView(R.id.imgNameStar4)
    ImageView imgNameStar4;

    @BindView(R.id.txtQuality1)
    TextView txtNameStar1;

    @BindView(R.id.txtQuality2)
    TextView txtNameStar2;

    @BindView(R.id.txtQuality3)
    TextView txtNameStar3;

    @BindView(R.id.txtQuality4)
    TextView txtNameStar4;


    RoundCornerProgressBar[] listProgress;
    ImageView[] listStarImage;
    TextView[] listTextQualityStars;


    protected final String TAG = getClass().getSimpleName();

    public static HeroStatInfo newInstance(Hero hero, HeroInfo mHeroInfo) {
        HeroStatInfo fragment = new HeroStatInfo();
        fragment.mHero = hero;
        fragment.mHeroInfo = mHeroInfo;
        return fragment;
    }


    private AdView mAdView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.hero_stats, container, false);

        //mAdView = (AdView) v.findViewById(R.id.ad_view);
        // Create an ad request. Check your logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("121EC3F83A2EAFBD46DB00F1773A13A0")
                .build();

        // Start loading the ad in the background.
        //mAdView.loadAd(adRequest);


        ButterKnife.bind(this, v);
        listProgress = new RoundCornerProgressBar[8];
        listProgress[0] = progressEasyOfUse;
        listProgress[1] = progressRange;
        listProgress[2] = progressAccuracy;
        listProgress[3] = progressPower;
        listProgress[4] = progressMobility;
        listProgress[5] = progressStamina;
        listProgress[6] = progressUtility;
        listProgress[7] = progressCrowdControl;

        listStarImage = new ImageView[4];
        listStarImage[0] = imgNameStar1;
        listStarImage[1] = imgNameStar2;
        listStarImage[2] = imgNameStar3;
        listStarImage[3] = imgNameStar4;

        listTextQualityStars = new TextView[4];
        listTextQualityStars[0] = txtNameStar1;
        listTextQualityStars[1] = txtNameStar2;
        listTextQualityStars[2] = txtNameStar3;
        listTextQualityStars[3] = txtNameStar4;

        return v;
    }

    private HeroInfo mHeroInfo;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateForConfigChanged();

        initImage();

        showHeroInfo(mHeroInfo);
    }

    private void initImage() {
        Context context = getActivity();
        View root = getView();
        if (root != null) {
            mImageView = (ImageView) root.findViewById(R.id.imgPortraitOverlay);

            ((ImageView) root.findViewById(R.id.imgPortraitOverlay)).setImageResource(R.drawable.heroprimaryportrait_overlay);
            Glide.with(context).load(SteamUtils.getHeroFullImage(mHero.getDotaId())).into(mImageView);
        }
    }


    @SuppressWarnings("deprecation")
    private void showHeroInfo(HeroInfo mHeroInfo) {
        View root = getView();
        if (root != null) {
            //((TextView)fragmentView.findViewById(R.id.title)).setText(mHero.getLocalizedName());
            //((TextView) root.findViewById(R.id.txtFaction)).setText(stats.getAlignment() == 0 ? "Radiant" : "Dire");
            //addHeroRoles(root);
            ((TextView) root.findViewById(R.id.txtLore)).setText(mHeroInfo.getLore());
            List<String> list8Progree = mHeroInfo.getProgress8Info();
            int index = 0;
            for (String strProgress : list8Progree) {
                setProgressValue(index, Integer.parseInt(strProgress.substring(0, strProgress.length() - 1)));
                index++;
            }

            Role5Info m5Info = mHeroInfo.getRole5Info();
            addHeroRoles(root, m5Info);

            List<String> modeRanking = mHeroInfo.getModeRanking();

            String[] valueQuality = getResources().getStringArray(R.array.quality_stars);

            for (int i = 0; i < modeRanking.size() - 1; i++) {
                int numberImage = Integer.parseInt(modeRanking.get(i));
                Drawable drawableValue = getResources().getDrawable(R.drawable.img1star);
                switch (numberImage) {
                    case 1:
                        drawableValue = getResources().getDrawable(R.drawable.img1star);
                        break;
                    case 2:
                        drawableValue = getResources().getDrawable(R.drawable.img2star);
                        break;
                    case 3:
                        drawableValue = getResources().getDrawable(R.drawable.img3star);
                        break;
                    case 4:
                        drawableValue = getResources().getDrawable(R.drawable.img4star);
                        break;
                    case 5:
                        drawableValue = getResources().getDrawable(R.drawable.img5star);
                        break;
                    case 6:
                        drawableValue = getResources().getDrawable(R.drawable.img6star);
                        break;
                    default:
                }
                listStarImage[i].setImageDrawable(drawableValue);
                listTextQualityStars[i].setText(valueQuality[numberImage]);
            }


        }
    }

    private void setProgressValue(int index, int numberProgress) {
        listProgress[index].setProgressColor(Color.parseColor("#0884F8"));
        listProgress[index].setProgressBackgroundColor(Color.parseColor("#DA5B14"));
        listProgress[index].setMax(100);
        listProgress[index].setProgress(numberProgress);
    }


    private void addHeroRoles(View root, Role5Info role5Info) {
        LinearLayout rolesHolder = (LinearLayout) root.findViewById(R.id.roles_holder);
        rolesHolder.removeAllViews();

        if (role5Info.getType() != null) {
            rolesHolder.addView(initRoleUI(role5Info.getType()));
        }
        if (role5Info.getRole() != null) {
            rolesHolder.addView(initRoleUI(role5Info.getRole()));
        }

        if (role5Info.getSpeed() != null) {
            rolesHolder.addView(initRoleUI(role5Info.getSpeed()));
        }

        if (role5Info.getHitpoints() != null) {
            rolesHolder.addView(initRoleUI(role5Info.getHitpoints()));
        }

        if (role5Info.getTier() != null) {
            rolesHolder.addView(initRoleUI(role5Info.getTier()));
        }
    }

    private View initRoleUI(String text) {
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                height);

        TextView textView = new TextView(getActivity());
        textView.setLayoutParams(layoutParams);
        textView.setText(text);
        textView.setTextColor(getResources().getColor(R.color.cmn_black));
        textView.setSingleLine(true);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setCompoundDrawablePadding(Utils.dpSize(getActivity(), 5));
        return textView;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateForConfigChanged();
    }

    private void updateForConfigChanged() {
        View root = getView();
        if (root != null) {
            LinearLayout portraitHolder = (LinearLayout) root.findViewById(R.id.portrait_holder1);
            LinearLayout mainHolder = (LinearLayout) root.findViewById(R.id.main_vertical);
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                mainHolder.setOrientation(LinearLayout.HORIZONTAL);
                portraitHolder.setOrientation(LinearLayout.VERTICAL);
            } else {
                mainHolder.setOrientation(LinearLayout.VERTICAL);
                portraitHolder.setOrientation(LinearLayout.HORIZONTAL);
            }
        }
    }

}
