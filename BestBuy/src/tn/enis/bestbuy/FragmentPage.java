package tn.enis.bestbuy;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentPage extends Fragment {

  private String title;
  private int    layoutId;

  public FragmentPage(String title, int layoutId) {
    this.title = title;
    this.layoutId = layoutId;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    /*
    // If you want the pages to have padding :p
    FrameLayout fl = new FrameLayout(getActivity());
    View myFragmentView = inflater.inflate(layoutId, container, false);
    LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    fl.setLayoutParams(params);
    int margin = (int) TypedValue
        .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources()
            .getDisplayMetrics());
    params.setMargins(margin, margin, margin, margin);
    myFragmentView.setLayoutParams(params);
    myFragmentView.setBackgroundResource(R.drawable.background_card);
    fl.addView(myFragmentView);
    */
    return inflater.inflate(layoutId, container, false);
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public int getLayoutId() {
    return layoutId;
  }

  public void setLayoutId(int layoutId) {
    this.layoutId = layoutId;
  }
}
