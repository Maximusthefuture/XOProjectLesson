package com.example.xoprojectlesson;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class XOCustomVIew extends View {

    private static final float STROKE = 10f;
    private static final String TAG = "XOCustomView";

    private Paint gridPaint = new Paint();
    private float xRatio = 1 / 3f;
    private Path mPath = new Path();
    private Paint mPaint = new Paint();
    private TicTacToeField mTicTacToeField;
    int COUNT = 3;
    private Drawable circle;
    private Drawable cross;
    private int row = 0;
    private int col = 0;
    private int cells = COUNT;
    private int size = 0;
    private int left = 0;
    private int right = 0;
    private int top = 0;
    private int bottom = 0;
    private boolean[][] winnerMatrix;
    private WinListener mWinListener = new WinListenerClass();
    private GameStatus mGameStatus = GameStatus.GAME;

    private TicTacToeField.Figure mFigure = TicTacToeField.Figure.NONE;


    public XOCustomVIew(Context context) {
        super(context);
        init(context);

    }

    public XOCustomVIew(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        init(context);
    }

    public XOCustomVIew(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void initPaint()  {
        mPaint.setColor(Color.BLACK);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(STROKE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.MITER);
        gridPaint.setColor(Color.BLACK);
        gridPaint.setStrokeWidth(STROKE);
        mTicTacToeField = new TicTacToeField(cells);

    }


    @Override
    protected void onDraw(Canvas canvas) {

        invalidate();
        switch (mGameStatus){
            case START:
                drawHorizontalLine(canvas);
                drawVerticalLine(canvas);
                circle.setBounds(getLeft(), getTop(), getRight(),  getBottom());
                canvas.drawPath(mPath, mPaint);
                drawFigures(canvas);
            case GAME_OVER:
                drawHorizontalLine(canvas);
                drawVerticalLine(canvas);
            case GAME:
            case RESTART:


        }
    }

    public void restart() {
        mGameStatus = GameStatus.START;
        mTicTacToeField = new TicTacToeField(cells);

    }

    private void init(Context context) {

        mGameStatus = GameStatus.START;
        circle = context.getDrawable(R.drawable.circle);
        cross = context.getDrawable(R.drawable.cross);

    }

    public interface WinListener {
        void onWin(TicTacToeField.Figure win);
    }

    public class WinListenerClass implements WinListener {

        @Override
        public void onWin(TicTacToeField.Figure win) {

        }
    }

    public void setOnWinListener(WinListener listener) {
        if (listener != null) {
            mWinListener = listener;
        } else {
            mWinListener = new WinListenerClass();
        }
    }

    private void drawFigures(Canvas canvas) {
        int cellSize = (int) (size - STROKE * (cells - 1) - (STROKE * 2)) / cells;
        Drawable drawable;
        for (int row = 0; row < cells; row++) {
            for (int col = 0; col < cells; col++) {
                int x = (int) (left + STROKE / 2+ (STROKE * col) + (cellSize * col));
                int y = (int) (top + STROKE / 2 + (STROKE * row) + (cellSize * row));

                if (!mTicTacToeField.isEmptyCell(row, col)) {
                    if (mTicTacToeField.getFigure(row, col) == TicTacToeField.Figure.CROSS) {
                        drawable = cross;
                    } else {
                        drawable = circle;
                    }
//
                    drawable.setBounds(x, y, x + cellSize, y + cellSize);
                    drawable.draw(canvas);
                }
            }
        }
}
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int cellSize = size / cells;
                int x = (int) (event.getX() - left);
                int y = (int) (event.getY() - top);

                int mRow = y / cellSize;
                int mCol = x / cellSize;

                spawnFigures(mRow, mCol, cellSize);
        }
        return true;

    }

    private void spawnFigures(int row, int col, int cellSize) {
        if (row >= 0 && row < cells && col >= 0 && col < cellSize) {
            TicTacToeField.Figure figure = (mFigure == TicTacToeField.Figure.CROSS) ? TicTacToeField.Figure.CIRCLE : TicTacToeField.Figure.CROSS;
            if (mTicTacToeField.setFigure(row, col, figure)) {
                mFigure = figure;
                isWin();
            }
            this.row = row;
            this.col = col;
        }
    }

    private boolean isWin() {
        TicTacToeField.Figure win = mTicTacToeField.getWinner();

        if (win.equals(TicTacToeField.Figure.NONE)) {
            if (mTicTacToeField.isFull()) {
                Toast.makeText(getContext(), "Ничья", Toast.LENGTH_SHORT).show();
                return true;
            }
        } else {
            mWinListener.onWin(win);
            winnerMatrix = mTicTacToeField.getWinnerMatrix();
            Toast.makeText(getContext(), "WIN: " + win, Toast.LENGTH_SHORT).show();
            mGameStatus = GameStatus.GAME_OVER;
            return true;
        }
        return false;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        size = Math.min(w, h );
        left = (w - size) / 2;
        top = (h - size) / 2;
        right = size + left;
        bottom = size + top;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = Math.min(getMeasuredHeight(), getMeasuredWidth());
        setMeasuredDimension(size, size);
    }

    public void drawVerticalLine(Canvas canvas) {
        canvas.drawLine(0f, 0f, 0f, (float)getHeight() * (4 * xRatio), gridPaint);
        canvas.drawLine((float)getWidth() * (2 * xRatio), 0f, (float)getWidth() * (2 * xRatio), (float)getHeight() * (4 * xRatio), mPaint);
        canvas.drawLine((float)getWidth() * xRatio , 0f, (float)getWidth()  * xRatio, (float)getHeight() * (4 * xRatio), mPaint);
        canvas.drawLine(getWidth(), 0f, getWidth(), (float)getHeight() * (4 * xRatio), gridPaint);
    }

    public void drawHorizontalLine(Canvas canvas) {

        canvas.drawLine(0f, 0f, getWidth(), 0f, gridPaint);
        canvas.drawLine(0f, (float) getHeight() / 2 * (2 * xRatio), getWidth(), (float) getHeight() / 2 * (2 * xRatio), mPaint);
        canvas.drawLine(0f, (float) getHeight() / 2 * (4 * xRatio), getWidth(), (float) getHeight() / 2 * (4 * xRatio), mPaint);
        canvas.drawLine(0f, (float) getHeight() * (3 * xRatio), getWidth(), (float) getHeight() * (3 * xRatio), gridPaint);
    }


    @Override
    protected Parcelable onSaveInstanceState() {
        SavedState state = new SavedState(super.onSaveInstanceState());
        state.mTacToeField = mTicTacToeField;
        state.figure = mFigure.ordinal();
        return state;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        SavedState myState = (SavedState) state;
        mTicTacToeField = myState.mTacToeField;
        mFigure = TicTacToeField.Figure.values()[myState.figure];

        if (isWin()) {
            mGameStatus = GameStatus.GAME_OVER;
        } else {
            mGameStatus = GameStatus.START;
        }
    }

    private enum GameStatus {
        START,
        GAME,
        GAME_OVER,
        RESTART
    }


    private static class SavedState extends BaseSavedState {
        private TicTacToeField mTacToeField;
        private int figure;

        public SavedState(Parcel source) {
            super(source);
            mTacToeField = source.readParcelable(getClass().getClassLoader());
            figure = source.readInt();
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeParcelable(mTacToeField, 0);
            out.writeInt(figure);
        }
    }

}
