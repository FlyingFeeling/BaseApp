package com.feeling.mybaseapp.utils.transform;

/**
 * Copyright (C) 2017 Wasabeef
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

import java.security.MessageDigest;

public class RadiusTransformation extends BitmapTransformation {

  private static final int VERSION = 1;
  private static final String ID = "com.feeling.webpimage.transform.RadiusTransformation." + VERSION;
  private static final byte[] ID_BYTES = ID.getBytes(CHARSET);

  public enum CornerType {
    ALL,
    TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT,
    TOP, BOTTOM, LEFT, RIGHT,
    OTHER_TOP_LEFT, OTHER_TOP_RIGHT, OTHER_BOTTOM_LEFT, OTHER_BOTTOM_RIGHT,
    DIAGONAL_FROM_TOP_LEFT, DIAGONAL_FROM_TOP_RIGHT
  }

  private int radius;
  private int diameter;
  private int margin;
  private CornerType cornerType;

  public RadiusTransformation(int radius, int margin) {
    this(radius, margin, CornerType.ALL);
  }

  public RadiusTransformation(int radius, int margin, CornerType cornerType) {
    this.radius = radius;
    this.diameter = this.radius * 2;
    this.margin = margin;
    this.cornerType = cornerType;
  }

  @Override
  protected Bitmap transform(@NonNull Context context, @NonNull BitmapPool pool,
                             @NonNull Bitmap toTransform, int outWidth, int outHeight) {
    // From ImageView/Bitmap.createScaledBitmap.
    final float scale;
    float dx = 0, dy = 0;
    Matrix m = new Matrix();
    if (toTransform.getWidth() * outHeight > outWidth * toTransform.getHeight()) {
      scale = (float) outHeight / (float) toTransform.getHeight();
      dx = (outWidth - toTransform.getWidth() * scale) * 0.5f;
    } else {
      scale = (float) outWidth / (float) toTransform.getWidth();
      dy = (outHeight - toTransform.getHeight() * scale) * 0.5f;
    }

    m.setScale(scale, scale);
    m.postTranslate((int) (dx + 0.5f), (int) (dy + 0.5f));

    Bitmap bitmap = pool.get(outWidth, outHeight, getSafeConfig(toTransform));
    bitmap.setHasAlpha(true);

    Canvas canvas = new Canvas(bitmap);
    Paint paint = new Paint();
    paint.setAntiAlias(true);
    BitmapShader bitmapShader=new BitmapShader(toTransform, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
    bitmapShader.setLocalMatrix(m);
    paint.setShader(bitmapShader);
    drawRoundRect(canvas, paint, outWidth, outHeight);
    return bitmap;
  }

  private void drawRoundRect(Canvas canvas, Paint paint, float width, float height) {
    float right = width - margin;
    float bottom = height - margin;

    switch (cornerType) {
      case ALL:
        canvas.drawRoundRect(new RectF(margin, margin, right, bottom), radius, radius, paint);
        break;
      case TOP_LEFT:
        drawTopLeftRoundRect(canvas, paint, right, bottom);
        break;
      case TOP_RIGHT:
        drawTopRightRoundRect(canvas, paint, right, bottom);
        break;
      case BOTTOM_LEFT:
        drawBottomLeftRoundRect(canvas, paint, right, bottom);
        break;
      case BOTTOM_RIGHT:
        drawBottomRightRoundRect(canvas, paint, right, bottom);
        break;
      case TOP:
        drawTopRoundRect(canvas, paint, right, bottom);
        break;
      case BOTTOM:
        drawBottomRoundRect(canvas, paint, right, bottom);
        break;
      case LEFT:
        drawLeftRoundRect(canvas, paint, right, bottom);
        break;
      case RIGHT:
        drawRightRoundRect(canvas, paint, right, bottom);
        break;
      case OTHER_TOP_LEFT:
        drawOtherTopLeftRoundRect(canvas, paint, right, bottom);
        break;
      case OTHER_TOP_RIGHT:
        drawOtherTopRightRoundRect(canvas, paint, right, bottom);
        break;
      case OTHER_BOTTOM_LEFT:
        drawOtherBottomLeftRoundRect(canvas, paint, right, bottom);
        break;
      case OTHER_BOTTOM_RIGHT:
        drawOtherBottomRightRoundRect(canvas, paint, right, bottom);
        break;
      case DIAGONAL_FROM_TOP_LEFT:
        drawDiagonalFromTopLeftRoundRect(canvas, paint, right, bottom);
        break;
      case DIAGONAL_FROM_TOP_RIGHT:
        drawDiagonalFromTopRightRoundRect(canvas, paint, right, bottom);
        break;
      default:
        canvas.drawRoundRect(new RectF(margin, margin, right, bottom), radius, radius, paint);
        break;
    }
  }

  private void drawTopLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
    canvas.drawRoundRect(new RectF(margin, margin, margin + diameter, margin + diameter),
        radius, radius, paint);
    canvas.drawRect(new RectF(margin, margin + radius, margin + radius, bottom), paint);
    canvas.drawRect(new RectF(margin + radius, margin, right, bottom), paint);
  }

  private void drawTopRightRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
    canvas.drawRoundRect(new RectF(right - diameter, margin, right, margin + diameter), radius,
        radius, paint);
    canvas.drawRect(new RectF(margin, margin, right - radius, bottom), paint);
    canvas.drawRect(new RectF(right - radius, margin + radius, right, bottom), paint);
  }

  private void drawBottomLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
    canvas.drawRoundRect(new RectF(margin, bottom - diameter, margin + diameter, bottom),
        radius, radius, paint);
    canvas.drawRect(new RectF(margin, margin, margin + diameter, bottom - radius), paint);
    canvas.drawRect(new RectF(margin + radius, margin, right, bottom), paint);
  }

  private void drawBottomRightRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
    canvas.drawRoundRect(new RectF(right - diameter, bottom - diameter, right, bottom), radius,
        radius, paint);
    canvas.drawRect(new RectF(margin, margin, right - radius, bottom), paint);
    canvas.drawRect(new RectF(right - radius, margin, right, bottom - radius), paint);
  }

  private void drawTopRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
    canvas.drawRoundRect(new RectF(margin, margin, right, margin + diameter), radius, radius,
        paint);
    canvas.drawRect(new RectF(margin, margin + radius, right, bottom), paint);
  }

  private void drawBottomRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
    canvas.drawRoundRect(new RectF(margin, bottom - diameter, right, bottom), radius, radius,
        paint);
    canvas.drawRect(new RectF(margin, margin, right, bottom - radius), paint);
  }

  private void drawLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
    canvas.drawRoundRect(new RectF(margin, margin, margin + diameter, bottom), radius, radius,
        paint);
    canvas.drawRect(new RectF(margin + radius, margin, right, bottom), paint);
  }

  private void drawRightRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
    canvas.drawRoundRect(new RectF(right - diameter, margin, right, bottom), radius, radius,
        paint);
    canvas.drawRect(new RectF(margin, margin, right - radius, bottom), paint);
  }

  private void drawOtherTopLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
    canvas.drawRoundRect(new RectF(margin, bottom - diameter, right, bottom), radius, radius,
        paint);
    canvas.drawRoundRect(new RectF(right - diameter, margin, right, bottom), radius, radius,
        paint);
    canvas.drawRect(new RectF(margin, margin, right - radius, bottom - radius), paint);
  }

  private void drawOtherTopRightRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
    canvas.drawRoundRect(new RectF(margin, margin, margin + diameter, bottom), radius, radius,
        paint);
    canvas.drawRoundRect(new RectF(margin, bottom - diameter, right, bottom), radius, radius,
        paint);
    canvas.drawRect(new RectF(margin + radius, margin, right, bottom - radius), paint);
  }

  private void drawOtherBottomLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
    canvas.drawRoundRect(new RectF(margin, margin, right, margin + diameter), radius, radius,
        paint);
    canvas.drawRoundRect(new RectF(right - diameter, margin, right, bottom), radius, radius,
        paint);
    canvas.drawRect(new RectF(margin, margin + radius, right - radius, bottom), paint);
  }

  private void drawOtherBottomRightRoundRect(Canvas canvas, Paint paint, float right,
                                             float bottom) {
    canvas.drawRoundRect(new RectF(margin, margin, right, margin + diameter), radius, radius,
        paint);
    canvas.drawRoundRect(new RectF(margin, margin, margin + diameter, bottom), radius, radius,
        paint);
    canvas.drawRect(new RectF(margin + radius, margin + radius, right, bottom), paint);
  }

  private void drawDiagonalFromTopLeftRoundRect(Canvas canvas, Paint paint, float right,
                                                float bottom) {
    canvas.drawRoundRect(new RectF(margin, margin, margin + diameter, margin + diameter),
        radius, radius, paint);
    canvas.drawRoundRect(new RectF(right - diameter, bottom - diameter, right, bottom), radius,
        radius, paint);
    canvas.drawRect(new RectF(margin, margin + radius, right - diameter, bottom), paint);
    canvas.drawRect(new RectF(margin + diameter, margin, right, bottom - radius), paint);
  }

  private void drawDiagonalFromTopRightRoundRect(Canvas canvas, Paint paint, float right,
                                                 float bottom) {
    canvas.drawRoundRect(new RectF(right - diameter, margin, right, margin + diameter), radius,
        radius, paint);
    canvas.drawRoundRect(new RectF(margin, bottom - diameter, margin + diameter, bottom),
        radius, radius, paint);
    canvas.drawRect(new RectF(margin, margin, right - radius, bottom - radius), paint);
    canvas.drawRect(new RectF(margin + radius, margin + radius, right, bottom), paint);
  }

  private static Bitmap.Config getSafeConfig(Bitmap bitmap) {
    return bitmap.getConfig() != null ? bitmap.getConfig() : Bitmap.Config.ARGB_8888;
  }

  @Override
  public String key() {
    return "RadiusTransformation(radius=" + radius + ", margin=" + margin + ", diameter="
        + diameter + ", cornerType=" + cornerType.name() + ")";
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof RadiusTransformation;
  }

  @Override
  public int hashCode() {
    return ID.hashCode();
  }

  @Override
  public void updateDiskCacheKey(MessageDigest messageDigest) {
    messageDigest.update(ID_BYTES);
  }
}
