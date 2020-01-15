package me.liam.helper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

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

    public static <T extends SupportFragment> T findFragmentByClass(FragmentManager fm, Class cls){
        for (Fragment f : fm.getFragments()){
            if (f instanceof SupportFragment && f.getClass().getName().equals(cls.getName())){
                return (T) f;
            }
        }
        return null;
    }

    public static void getAllFragments(List<SupportFragment> list, FragmentManager fm){
        for (Fragment f: fm.getFragments()){
            if (f instanceof SupportFragment && !f.isRemoving() && !f.isDetached()){
                list.add((SupportFragment) f);
                if (f.getChildFragmentManager().getFragments().size() != 0){
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

    public static List<SupportFragment> getActiveList(FragmentManager fm){
        List<SupportFragment> list = new ArrayList<>();
        for (Fragment f : fm.getFragments()){
            if (f instanceof SupportFragment
                    && f.isAdded()
                    && f.isVisible()
                    && !f.isRemoving()
                    && !f.isDetached()
                    && f.isResumed()){
                list.add((SupportFragment) f);
            }
        }
        return list;
    }

    public static SupportFragment getLastActiveFragment(FragmentManager fm){
        LinkedList<SupportFragment> linkedList = new LinkedList<>();
        linkedList.addAll(getActiveList(fm));
        try {
            return linkedList.getLast();
        }catch (NoSuchElementException e){
            return null;
        }
    }

    public static SupportFragment getLastFragment(FragmentManager fm){
        LinkedList<SupportFragment> linkedList = new LinkedList<>();
        linkedList.addAll(getInManagerFragments(fm));
        try {
            return linkedList.getLast();
        }catch (NoSuchElementException e){
            return null;
        }
    }

    public static SupportFragment getBeforeOne(List<SupportFragment> list, SupportFragment who){
        if (list == null || list.isEmpty() || who == null) return null;
        int index = list.indexOf(who);
        if (index == -1) return null;
        for (int i = index - 1; i >= 0; i --) {
            return list.get(i);
        }
        return null;
    }

    public static LinkedList<SupportFragment> getBackStackFragments(FragmentManager fm){
        LinkedList<SupportFragment> linkedList = new LinkedList<>();
        for (Fragment f : fm.getFragments()){
            if (f instanceof SupportFragment
                    && !f.isRemoving()
                    && !f.isDetached()
                    && ((SupportFragment)f).isBackStack()){
                linkedList.add((SupportFragment)f);
            }
        }
        return linkedList;
    }
}
