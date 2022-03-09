package com.reactnativecommunity.picker

import android.content.res.Resources
import android.graphics.Color
import android.graphics.Typeface
import android.util.TypedValue
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.UIManagerModule
import com.facebook.react.uimanager.annotations.ReactProp
import com.github.gzuliyujiang.wheelview.contract.OnWheelChangedListener
import com.github.gzuliyujiang.wheelview.widget.WheelView


@ReactModule(name = ReactIOSStylePickerManager.REACT_CLASS)
class ReactIOSStylePickerManager : SimpleViewManager<WheelView>() {

    companion object {
        const val REACT_CLASS = "RNCIOSStylePicker"
    }

    override fun getName(): String = REACT_CLASS

    override fun addEventEmitters(reactContext: ThemedReactContext, view: WheelView) {
        super.addEventEmitters(reactContext, view)
        view.setOnWheelChangedListener(object: OnWheelChangedListener {
            override fun onWheelScrolled(view: WheelView?, offset: Int) {
            }

            override fun onWheelSelected(view: WheelView?, position: Int) {
                if (view != null) {
                    reactContext.getNativeModule(UIManagerModule::class.java)!!.eventDispatcher.dispatchEvent(PickerItemSelectEvent(
                        view.id, position))
                }
            }

            override fun onWheelScrollStateChanged(view: WheelView?, state: Int) {
            }

            override fun onWheelLoopFinished(view: WheelView?) {
            }

        })
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
        view.setDefaultPosition(selected)
    }

    @ReactProp(name = "items")
    fun setItems(view: WheelView, items: ReadableArray?) {
        if (items != null) {
            view.isCyclicEnabled = false
            view.curvedMaxAngle = 15
            view.isCurvedEnabled = true
            view.isCurtainEnabled = false
            view.isIndicatorEnabled = false
            view.isAtmosphericEnabled = true
            view.selectedTextBold = false;


            val item: ReadableMap = items.getMap(0)
            if (item.hasKey("style") && !item.isNull("style")) {
                val style: ReadableMap = item.getMap("style")!!
                if (style.hasKey("color") && !style.isNull("color")) {
                    val centerColor = style.getInt("color")
                    view.selectedTextColor = centerColor
                }
                if (style.hasKey("fontFamily") && !style.isNull("fontFamily")) {
                    val face = Typeface.createFromAsset(view.context.assets,
                            "fonts/" + style.getString("fontFamily") + ".ttf")
                    view.typeface = face
                }
                if (style.hasKey("fontSize") && !style.isNull("fontSize")) {
                    val dip = style.getDouble("fontSize").toFloat()
                    val px = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        dip,
                        view.resources.displayMetrics
                    )
                    view.textSize = px
                    view.selectedTextSize = px
                }
                if (style.hasKey("backgroundColor") && !style.isNull("backgroundColor")) {
                    view.setBackgroundColor(style.getInt("backgroundColor"))
                } else {
                    view.setBackgroundColor(Color.TRANSPARENT)
                }
            }
            view.data = items.toArrayList().map { (it as HashMap<*, *>)["label"] }
        }
    }


}