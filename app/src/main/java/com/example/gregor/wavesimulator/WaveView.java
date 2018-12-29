package com.example.gregor.wavesimulator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import Solver.CPPSimulator;
import Solver.ValleyDetection;

public class WaveView extends View {
    private static Rect[][] drawing_rects;
    private Paint paint;

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();

        if (CPPSimulator.sim == null) {
            CPPSimulator.sim = new CPPSimulator();
        }
        drawing_rects = new Rect[CPPSimulator.cell_count][CPPSimulator.cell_count];
        int size_x = getWidth() / CPPSimulator.cell_count;
        int offset_x = (getWidth() - size_x * CPPSimulator.cell_count) / 2;
        int size_y = getHeight() / CPPSimulator.cell_count;
        int offset_y = (getHeight() - size_y * CPPSimulator.cell_count) / 2;
        for (int i = 0; i < CPPSimulator.cell_count; i++) {
            for (int j = 0; j < CPPSimulator.cell_count; j++) {
                drawing_rects[i][j] = new Rect(offset_x + i * size_x, offset_y + j * size_y, offset_x + (i + 1) * size_x, offset_y + (j + 1) * size_y);
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int old_w, int old_h) {
        drawing_rects = new Rect[CPPSimulator.cell_count][CPPSimulator.cell_count];
        int size_x = w / CPPSimulator.cell_count;
        int offset_x = (w - size_x * CPPSimulator.cell_count) / 2;
        int size_y = h / CPPSimulator.cell_count;
        int offset_y = (h - size_y * CPPSimulator.cell_count) / 2;
        for (int i = 0; i < CPPSimulator.cell_count; i++) {
            for (int j = 0; j < CPPSimulator.cell_count; j++) {
                drawing_rects[i][j] = new Rect(offset_x + i * size_x, offset_y + j * size_y, offset_x + (i + 1) * size_x, offset_y + (j + 1) * size_y);
            }
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        /* Start of edge detection implementation
        double[][] input = new double[CPPSimulator.cell_count + 2][CPPSimulator.cell_count + 2];
        for (int i = 0; i < CPPSimulator.cell_count; i++) {
            for (int j = 0; j < CPPSimulator.cell_count; j++) {
                input[i + 1][j + 1] = CPPSimulator.sim.getHeight(i, j);
            }
        }
        double[][] edgedetect_lr = {{1, 1, 1}, {0, 0, 0}, {-1, -1, -1}};
        double[][] edgedetect_tb = {{1, 0, -1}, {1, 0, -1}, {1, 0, -1}};
        double[][] output_lr = ValleyDetection.convolution2D(input, CPPSimulator.cell_count + 2, CPPSimulator.cell_count + 2, edgedetect_lr, 3, 3);
        double[][] output_tb = ValleyDetection.convolution2D(input, CPPSimulator.cell_count + 2, CPPSimulator.cell_count + 2, edgedetect_tb, 3, 3);
        Todo: Implement proper detection of wave valleys and mountains*/
        for (int i = 0; i < CPPSimulator.cell_count; i++) {
            for (int j = 0; j < CPPSimulator.cell_count; j++) {
                int current = (int) CPPSimulator.sim.getHeight(i, j) * 25;
                //int current_der = (int) (Math.max(output_lr[i][j], output_tb[i][j]));
                current = (current > 255) ? 255 : current;
                if ((int) CPPSimulator.sim.getBathymetry(i, j) != 0) {
                    paint.setColor(Color.rgb(102, 51, 0));
                    canvas.drawRect(drawing_rects[i][j], paint);
                } else {
                    paint.setColor(Color.rgb(0, 255 - current, 255));
                    canvas.drawRect(drawing_rects[i][j], paint);
                }
            }
        }
    }

}
