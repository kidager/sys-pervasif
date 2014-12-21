package tn.enis.bestbuy;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {

  private List<FragmentPage> fragments;

  public PagerAdapter(FragmentManager fm, List<FragmentPage> fragments) {
    super(fm);
    this.fragments = fragments;
  }

  @Override
  public CharSequence getPageTitle(int position) {
    return fragments.get(position).getTitle();
  }

  @Override
  public int getCount() {
    return fragments.size();
  }

  @Override
  public Fragment getItem(int position) {
    return fragments.get(position);
  }

}
