package com.mrprona.brawlassistant.hero.fragment.guide;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mrprona.brawlassistant.BeanContainer;
import com.mrprona.brawlassistant.R;
import com.mrprona.brawlassistant.base.util.FileUtils;
import com.mrprona.brawlassistant.hero.api.abilities.Ability;
import com.mrprona.brawlassistant.hero.api.guide.custom.AbilityBuild;
import com.mrprona.brawlassistant.hero.api.guide.custom.Guide;
import com.mrprona.brawlassistant.hero.service.HeroService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Histler
 * Date: 28.01.14
 */
public class AbilityBuildPart extends Fragment implements GuideHolder {
    private AbilityBuild abilityBuild;

    public static AbilityBuildPart newInstance(long heroId, AbilityBuild abilityBuild) {
        AbilityBuildPart fragment = new AbilityBuildPart();
        Bundle bundle = new Bundle();
        bundle.putLong("id", heroId);
        bundle.putSerializable("abilityBuild", abilityBuild);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.guide_skillbuild, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        abilityBuild = (AbilityBuild) getArguments().get("abilityBuild");
        initAbilities();
    }

    @SuppressWarnings("deprecation")
    private void initAbilities() {
        View root = getView();
        if (root != null) {
            ViewGroup abilityHolder = (ViewGroup) root.findViewById(R.id.ability_holder);
            abilityHolder.removeAllViews();
            Activity activity = getActivity();
            if (abilityBuild != null && activity != null) {
                Map<String, String> abilityUpgrades = abilityBuild.getOrder();
                if (abilityUpgrades != null && abilityUpgrades.size() > 0) {

                    HeroService heroService = BeanContainer.getInstance().getHeroService();
                    long heroId = getArguments().getLong("id");
                    List<Ability> abilities = heroService.getHeroAbilities(activity, heroId);
                    Map<Ability, String> tags = new HashMap<Ability, String>();

                    for (int i = 0; i < abilities.size(); i++) {
                        Ability ability = abilities.get(i);
                        ImageView currentAbilityHeader = (ImageView) root.findViewWithTag(String.valueOf(i));
                        currentAbilityHeader.setImageBitmap(FileUtils
                                .getBitmapFromAsset(getActivity(), heroService.getAbilityPath(activity, ability.getId())));
                        tags.put(ability, String.valueOf(i));
                    }
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    for (String level : abilityUpgrades.keySet()) {
                        View row = inflater.inflate(R.layout.skillbuild_attribute_table_row, null, false);
                        String abilityName = abilityUpgrades.get(level);
                        Ability tekAbility = new Ability();
                        tekAbility.setName(abilityName);
                        String tag = tags.get(tekAbility);

                        TextView upgraded = (TextView) row.findViewWithTag(tag);
                        // System.out.println("dota2 info on ability:" + abilityUpgrade.getAbility() + " on level:" + abilityUpgrade.getLevel());
                        if (upgraded == null) {
                            System.out.println(level);
                        } else {
                            upgraded.setText(level);
                            upgraded.setBackgroundDrawable(getResources().getDrawable(R.drawable.attribute_selected_bkg));
                        }
                        abilityHolder.addView(row);
                    }
                } else {
                    abilityHolder.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void updateWith(Guide guide) {
        if (guide != null) {
            abilityBuild = guide.getAbilityBuild();
            getArguments().putSerializable("abilityBuild", abilityBuild);
            if (getActivity() != null) {
                initAbilities();
            }
        }
    }
}
