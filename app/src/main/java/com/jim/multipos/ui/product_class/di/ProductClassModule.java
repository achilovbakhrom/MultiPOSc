package com.jim.multipos.ui.product_class.di;

import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.ui.ActivityScope;
import com.jim.multipos.ui.product_class.ProductClassActivity;
import com.jim.multipos.ui.product_class.presenters.AddProductClassPresenter;
import com.jim.multipos.ui.product_class.presenters.AddProductClassPresenterImpl;
import com.jim.multipos.ui.product_class.presenters.ProductClassListPresenter;
import com.jim.multipos.ui.product_class.presenters.ProductClassListPresenterImpl;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.managers.PosFragmentManager;

import dagger.Module;
import dagger.Provides;

/**
 * Created by developer on 29.08.2017.
 */
@Module
public class ProductClassModule {
    private ProductClassActivity productClassActivity;

    public ProductClassModule(ProductClassActivity productClassActivity){
        this.productClassActivity = productClassActivity;
    }

    @Provides
    @ActivityScope
    public ProductClassActivity provideProductClassActivity(){
        return productClassActivity;
    }



    @ActivityScope
    @Provides
    public PosFragmentManager getPosFragmentManager(ProductClassActivity activity) {
        return new PosFragmentManager(activity);
    }
//    @ActivityScope
//    @Provides
//    public AddProductClassPresenter provideAddProductClassPresenter(RxBus rxBus, DatabaseManager databaseManager,RxBusLocal rxBusLocal){
//        return new AddProductClassPresenterImpl(rxBus,databaseManager,rxBusLocal);
//    }
//    @ActivityScope
//    @Provides
//    public ProductClassListPresenter provideProductClassListPresenter(DatabaseManager databaseManager,RxBusLocal rxBusLocal, RxBus rxBus){
//        return new ProductClassListPresenterImpl(databaseManager,rxBusLocal,rxBus);
//    }
    @ActivityScope
    @Provides
    public RxBusLocal provideRxBusLocal(){
        return new RxBusLocal();
    }


}
