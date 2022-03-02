package com.reactnativecommunity.picker

import android.graphics.Color
import android.graphics.Typeface
import com.contrarywind.adapter.WheelAdapter
import com.contrarywind.view.WheelView
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.UIManagerModule
import com.facebook.react.uimanager.annotations.ReactProp

@ReactModule(name = ReactIOSStylePickerManager.REACT_CLASS)
class ReactIOSStylePickerManager : SimpleViewManager<WheelView>() {

    companion object {
        const val REACT_CLASS = "RNCIOSStylePicker"
    }

    override fun getName(): String = REACT_CLASS

    override fun addEventEmitters(reactContext: ThemedReactContext, view: WheelView) {
        super.addEventEmitters(reactContext, view)
        view.setOnItemSelectedListener {
            reactContext.getNativeModule(UIManagerModule::class.java)!!.eventDispatcher.dispatchEvent(PickerItemSelectEvent(
                    view.id, it))
        }
    }

    override fun createViewInstance(reactContext: ThemedReactContext): WheelView {
        return WheelView(reactContext.currentActivity)
    }

//    @ReactProp(name = ViewProps.NUMBER_OF_LINES, defaultInt = 1)
//    fun setNumberOfLines(view: WheelView, numberOfLines: Int) {
//        if (view.adapter == null) {
//            view.adapter = ReactIOSStylePickerAdapter(arrayListOf<String>())
//            return
//        }
//    }

    @ReactProp(name = "selected")
    fun setSelected(view: WheelView, selected: Int) {
        view.currentItem = selected
    }

    @ReactProp(name = "items")
    fun setItems(view: WheelView, items: ReadableArray?) {
        if (items != null) {
            view.setCyclic(false)

            val item: ReadableMap = items.getMap(0)
            if (item.hasKey("style") && !item.isNull("style")) {
                val style: ReadableMap = item.getMap("style")!!
                if (style.hasKey("color") && !style.isNull("color")) {
                    val centerColor = Color.parseColor(style.getString("color"))
                    view.setTextColorCenter(centerColor)
                    view.setTextColorOut(centerColor)
                }
                if (style.hasKey("fontFamily") && !style.isNull("fontFamily")) {
                    val face = Typeface.createFromAsset(view.context.assets,
                            "fonts/" + style.getString("fontFamily") + ".ttf")
                    view.setTypeface(face)
                }
                if (style.hasKey("fontSize") && !style.isNull("fontSize")) {
                    view.setTextSize(style.getDouble("fontSize").toFloat())
                }
                if (style.hasKey("backgroundColor") && !style.isNull("backgroundColor")) {
                    view.setBackgroundColor(style.getInt("backgroundColor"))
                } else {
                    view.setBackgroundColor(Color.TRANSPARENT)
                }
            }
            view.adapter = ReactIOSStylePickerAdapter(items.toArrayList().map { (it as HashMap<*, *>)["label"] })
        }
    }


}

class ReactIOSStylePickerAdapter<T>(private val dataItems: List<T>) : WheelAdapter<T> {


    override fun getItemsCount(): Int = this.dataItems.count()

    override fun getItem(index: Int): T = this.dataItems[index]

    override fun indexOf(o: T): Int = this.dataItems.indexOf(o)

}