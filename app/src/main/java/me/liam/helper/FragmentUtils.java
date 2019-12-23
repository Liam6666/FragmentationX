package me.liam.helper;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import me.liam.support.SupportFragment;

public class FragmentUtils {

    public static List<SupportFragment> getInManagerFragments(FragmentManager fm){
        List<SupportFragment> list = new ArrayList<>();
        if (fm == null) return list;
        for (Fragment f : fm.getFragments()){
            if (f instanceof SupportFragment
                    && !f.isRemoving()
                    && !f.isDetached()){
                list.add((SupportFragment) f);
            }
        }
        return list;
    }

    public static <T extends Fragment> T findFragmentByClass(FragmentManager fm, Class cls){
        for (SupportFragment f : getInManagerFragments(fm)){
            if (f.getClass().getName().equals(cls.getClass().getName())){
                return (T) f;
            }
        }
        return null;
    }

    public static void getAllFragments(List<SupportFragment> list, FragmentManager fm){
        for (Fragment f: fm.getFragments()){
            if (f instanceof SupportFragment && !f.isRemoving() && !f.isDetached()){
                list.add((SupportFragment) f);
                if (f.getChildFragmentManager().getFragments() != null
                        && f.getChildFragmentManager().getFragments().size() != 0){
                    getAllFragments(list,f.getChildFragmentManager());
                }
            }
        }
    }

    public static SupportFragment getBeforeOne(FragmentManager fm, SupportFragment who){
        if (fm == null || who == null) return null;
        List<SupportFragment> list = new ArrayList<>();
        for (Fragment f : fm.getFragments()){
            if (f instanceof SupportFragment && !f.isRemoving() && !f.isDetached()){
                list.add((SupportFragment) f);
            }
        }
        int index = list.indexOf(who);
        if (index == -1) return null;
        for (int i = index - 1; i >= 0; i --) {
            return list.get(i);
        }
        return null;
    }


    public static SupportFragment getTopOne(FragmentManager fm){
        List<SupportFragment> list = new ArrayList<>();
        for (Fragment f : fm.getFragments()){
            if (f instanceof SupportFragment && !f.isRemoving() && !f.isDetached()){
                list.add((SupportFragment) f);
            }
        }
        if (list.size() == 0) return null;
        return list.get(0);
    }


    public static SupportFragment getLastOne(FragmentManager fm){
        List<SupportFragment> list = getInManagerFragments(fm);
       for (int i = list.size() - 1; i >= 0; i --){
           return list.get(i);
       }
       return null;
    }
}
