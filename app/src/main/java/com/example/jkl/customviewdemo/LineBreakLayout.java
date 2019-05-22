package com.example.jkl.customviewdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * 自动换行的标签容器的自定义view
 *
 * @author jack 2018-07-02
 */
public class LineBreakLayout extends ViewGroup {
    private final static String TAG = "LineBreakLayout";
    /**
     * 所有标签
     */
    private Map<String, String> mAllLabels = new LinkedHashMap<>();
    /**
     * 选中标签
     */
    private List<String> mLabelSelected = new ArrayList<>();

    /**
     * 记录每一行数据
     */
    private List<LineView> mLineViewGroup;

    /**
     * 自定义属性
     */
    private int mLeftRightSpace;
    private int mRowSpace;
    private int mLabelLayoutId;
    private int mLabelLayoutId2;
    private int mLabelGravity;

    private LayoutInflater mLayoutInflater;

    private LabelOnClickListener mLabelOnClickListener;

    public LineBreakLayout(Context context) {
        this(context, null);
    }

    public LineBreakLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineBreakLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LineBreakLayout);
        mLeftRightSpace = ta.getDimensionPixelSize(R.styleable.LineBreakLayout_leftAndRightSpace, 10);
        mRowSpace = ta.getDimensionPixelSize(R.styleable.LineBreakLayout_rowSpace, 10);
        mLabelLayoutId = ta.getResourceId(R.styleable.LineBreakLayout_labelLayout, R.layout.item_job_subsrcibe_label_x);
        mLabelLayoutId2 = ta.getResourceId(R.styleable.LineBreakLayout_labelLayout2, R.layout.item_job_subsrcibe_label_no_x);
        mLabelGravity = ta.getInt(R.styleable.LineBreakLayout_labelGravity, LabelGravity.LEFT);
        ta.recycle(); //回收
    }

    /**
     * 更新标签
     *
     * @param isReverseOrder 是否倒序 true倒序，false不倒序
     */
    public void updateLabels(boolean isReverseOrder) {
        if (this.mAllLabels == null) {
            this.mAllLabels = new LinkedHashMap<>();
        }

        if (mAllLabels != null && mAllLabels.size() > 0) {
            removeAllViews();//移除所有的view
            mLayoutInflater = LayoutInflater.from(getContext());
            if (isReverseOrder) {
                //LinkedHashMap 逆序遍历
                ListIterator<Map.Entry<String, String>> i = new ArrayList<Map.Entry<String, String>>(
                        mAllLabels.entrySet()).listIterator(mAllLabels.size());
                while (i.hasPrevious()) {
                    Map.Entry<String, String> entry = i.previous();
                    setAndAddLabels(entry.getKey());
                }
            } else {
                Iterator<String> iterator = mAllLabels.keySet().iterator();
                while (iterator.hasNext()) {
                    setAndAddLabels(iterator.next());
                }
            }
        }
    }

    private void setAndAddLabels(final String key) {
        //获取标签布局
        final TextView tv = (TextView) mLayoutInflater.inflate(mLabelLayoutId, null);
        final String value = mAllLabels.get(key);
        tv.setText(value);
        //设置选中效果
        if (mLabelSelected.contains(key)) {
            //选中
            tv.setSelected(true);
        } else {
            //未选中
            tv.setSelected(false);
        }
        //点击标签后，重置选中效果
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setSelected(tv.isSelected() ? false : true);
                if (tv.isSelected()) {
                    //将选中的标签加入到lableSelected中
                    mLabelSelected.add(key);
                } else {
                    mLabelSelected.remove(key);
                }

                if (mLabelOnClickListener != null) {
                    mLabelOnClickListener.labelOnClick(tv, key, value, indexOfChild(tv));
                }
            }
        });
        tv.setTag(key);
        //将标签添加到容器中
        addView(tv);
    }

    /**
     * 在特定位置增加一个标签
     *
     * @param key
     * @param value
     */
    public void addLabel(final String key, final String value, int index) {
        final TextView tv;
        if (this.mAllLabels == null) {
            this.mAllLabels = new LinkedHashMap<>();
        }

        this.mAllLabels.put(key, value);

        LayoutInflater inflater = LayoutInflater.from(getContext());

        //获取标签布局
        tv = (TextView) inflater.inflate(mLabelLayoutId, null);

        tv.setText(value);
        //设置选中效果
        if (mLabelSelected.contains(key)) {
            //选中
            tv.setSelected(true);
        } else {
            //未选中
            tv.setSelected(false);
        }
        //点击标签后，重置选中效果
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setSelected(tv.isSelected() ? false : true);
                if (tv.isSelected()) {
                    //将选中的标签加入到lableSelected中
                    mLabelSelected.add(key);
                } else {
                    mLabelSelected.remove(key);
                }

                if (mLabelOnClickListener != null) {
                    mLabelOnClickListener.labelOnClick(tv, key, value, indexOfChild(tv));
                }
            }
        });
        tv.setTag(key);
        //将标签添加到容器中
        addView(tv, index);
    }


    /**
     * 在最前面增加一个标签
     *
     * @param key
     * @param value
     */
    public void addLabelAtFirstLabel(String key, String value) {

        if (this.mAllLabels == null) {
            this.mAllLabels = new LinkedHashMap<>();
        }
        this.mAllLabels.put(key, value);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        TextView tv = (TextView) inflater.inflate(mLabelLayoutId2, null);
        tv.setText(value);
        tv.setTag(key);
        addView(tv, 0);
    }

    /**
     * 在最后增加一个标签
     *
     * @param key
     * @param value
     */
    public void addLabelAtLast(String key, String value) {
        if (mAllLabels == null) {
            addLabel(key, value, 0);
        } else {
            addLabel(key, value, mAllLabels.size());
        }
    }


    public View getLabelView(String key) {
        return findViewWithTag(key);
    }

    public void removeLabel(String key) {
        View view = findViewWithTag(key);
        if (view != null) {
            removeView(view);
        } else {
            Log.e(TAG, "removeLabel view == null");
        }
        mAllLabels.remove(key);
    }

    public void removeLabel(String key, int index) {
        removeViewAt(index);
        mAllLabels.remove(key);
    }

    public void removeLabel(String key, View view) {
        removeView(view);
        mAllLabels.remove(key);
    }

    public void setLabelEnabled(String key, boolean bool) {
        View view = findViewWithTag(key);
        if (view != null) {
            setLabelEnabled(view, bool);
        } else {
            Log.e(TAG, "view == null");
        }
    }

    public void setLabelEnabled(View view, boolean bool) {
        view.setEnabled(bool);
    }

    /**
     * 更新标签是否可点击
     *
     * @param map 选中的标签，即不能点击，其他的都应该能点击
     */
    public void updateLabelEnabled(Map<String, String> map) {
        for (String key : mAllLabels.keySet()) {
            if (map.containsKey(key)) {
                setLabelEnabled(key, false);
            } else {
                setLabelEnabled(key, true);
            }
        }
    }

    /**
     * 获取选中标签
     */
    public List<String> getSelectedLabels() {
        return mLabelSelected;
    }

    /**
     * 获取所有标签
     */
    public Map<String, String> getAllLabels() {
        return mAllLabels;
    }

    public void setLabelOnClickListener(LabelOnClickListener listener) {
        mLabelOnClickListener = listener;
    }

    public interface LabelOnClickListener {
        void labelOnClick(View view, String key, String value, int index);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int withMode = MeasureSpec.getMode(widthMeasureSpec);
        int withSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int with = 0;
        int height = 0;
        int childCount = getChildCount();
        //在调用childView。getMeasre之前必须先调用该行代码，用于对子View大小的测量
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        //计算宽度
        switch (withMode) {
            case MeasureSpec.EXACTLY:
                with = withSize;
                break;
            case MeasureSpec.AT_MOST:
                for (int i = 0; i < childCount; i++) {
                    if (i != 0) {
                        with += mLeftRightSpace;
                    }
                    with += getChildAt(i).getMeasuredWidth();
                }
                with += getPaddingLeft() + getPaddingRight();
                with = with > withSize ? withSize : with;
                break;
            case MeasureSpec.UNSPECIFIED:
                for (int i = 0; i < childCount; i++) {
                    if (i != 0) {
                        with += mLeftRightSpace;
                    }
                    with += getChildAt(i).getMeasuredWidth();
                }
                with += getPaddingLeft() + getPaddingRight();
                break;
            default:
                with = withSize;
                break;

        }
        //根据计算出的宽度，计算出所需要的行数
        LineView mLineView = new LineView();
        //不能够在定义属性时初始化，因为onMeasure方法会多次调用
        mLineViewGroup = new ArrayList<>();
        for (int i = 0; i < childCount; i++) {
            if (mLineView.lineWidth + getChildAt(i).getMeasuredWidth() + mLeftRightSpace > with) {
                //每行的第一个子View很长，只能放下一个
                if (mLineView.lineViewList.size() == 0) {
                    mLineView.addView(getChildAt(i));
                    mLineViewGroup.add(mLineView);
                    mLineView = new LineView();
                } else {
                    mLineViewGroup.add(mLineView);
                    mLineView = new LineView();
                    mLineView.addView(getChildAt(i));
                }
            } else {
                mLineView.addView(getChildAt(i));
            }
        }
        //添加最后一行
        if (mLineView.lineViewList.size() > 0 && !mLineViewGroup.contains(mLineView)) {
            mLineViewGroup.add(mLineView);
        }
        //计算高度
        height = getPaddingTop() + getPaddingBottom();
        for (int i = 0; i < mLineViewGroup.size(); i++) {
            if (i != 0) {
                height += mRowSpace;
            }
            height += mLineViewGroup.get(i).lineHeight;
        }
        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                height = heightSize;
                break;
            case MeasureSpec.AT_MOST:
                height = height > heightSize ? heightSize : height;
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
            default:
                break;
        }
        setMeasuredDimension(with, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        t = getPaddingTop();
        for (int i = 0; i < mLineViewGroup.size(); i++) {
            int left = getPaddingLeft();
            LineView mLineView = mLineViewGroup.get(i);
            int lastWidth = getMeasuredWidth() - mLineView.lineWidth;
            for (int j = 0; j < mLineView.lineViewList.size(); j++) {
                View view = mLineView.lineViewList.get(j);

                switch (mLabelGravity) {
                    case LabelGravity.RIGHT:
                        //标签右对齐
                        view.layout(left + lastWidth, t, left + lastWidth + view.getMeasuredWidth(), t + view.getMeasuredHeight());
                        break;
                    case LabelGravity.CENTER:
                        //标签居中对齐
                        view.layout(left + lastWidth / 2, t, left + lastWidth / 2 + view.getMeasuredWidth(), t + view.getMeasuredHeight());
                        break;
                    case LabelGravity.LEFT:
                        //标签左对齐
                        view.layout(left, t, left + view.getMeasuredWidth(), t + view.getMeasuredHeight());
                        break;
                    default:
                        break;
                }
                left += view.getMeasuredWidth() + mLeftRightSpace;
            }
            t += mLineView.lineHeight + mRowSpace;
        }
    }

    /**
     * 用于存放一行子View
     */
    private final class LineView {
        private List<View> lineViewList = new ArrayList<>();

        /**
         * 当前行中所需要占用的宽度
         */
        private int lineWidth = getPaddingLeft() + getPaddingRight();

        /**
         * 该行View中所需要占用的最大高度
         */
        private int lineHeight = 0;

        private void addView(View view) {
            if (lineViewList.size() != 0) {
                lineWidth += mLeftRightSpace;
            }
            lineHeight = lineHeight > view.getMeasuredHeight() ? lineHeight : view.getMeasuredHeight();
            lineWidth += view.getMeasuredWidth();
            lineViewList.add(view);
        }
    }

    /**
     * 每行子View的对齐方式
     */
    private final static class LabelGravity {
        private final static int RIGHT = 0;
        private final static int LEFT = 1;
        private final static int CENTER = 2;


    }
}