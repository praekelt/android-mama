package za.foundation.praekelt.mama.inject.module;

import dagger.Module;
import dagger.Provides;
import za.foundation.praekelt.mama.app.App;
import za.foundation.praekelt.mama.app.fragment.CategoryListFragment;
import za.foundation.praekelt.mama.app.viewmodel.BaseViewModel;
import za.foundation.praekelt.mama.app.viewmodel.CategoryListFragmentViewModel;

/**
 * Created by eduardokolomajr on 2015/09/10.
 */
@Module
public class CategoryListFragmentModule {
    private CategoryListFragment frag;

    public CategoryListFragmentModule(CategoryListFragment frag) {
        this.frag = frag;
    }

    @Provides
    public CategoryListFragmentViewModel provideViewModel() {
        BaseViewModel cached = ((App) frag.getActivity().getApplication())
                .getCachedViewModel(CategoryListFragmentViewModel.TAG + frag.getUuid());
        if (cached != null)
            return (CategoryListFragmentViewModel) cached;
        else
            return new CategoryListFragmentViewModel(frag);
    }
}
