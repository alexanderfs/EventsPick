package com.alexan.findevents.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.alexan.findevents.R;
import com.alexan.findevents.dao.DBEvent;
import com.alexan.findevents.dao.DBPickEvent;


public class ImageUtil {
	
	private static String defaultDatePattern = "yyyy-MM-dd";
	
	public static Date parse(String strDate)    
    {  
        try {
			return new SimpleDateFormat(defaultDatePattern).parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;  
    }
	
	public static boolean getSDCardStatus() {
		String status = Environment.getExternalStorageState();
		  if (status.equals(Environment.MEDIA_MOUNTED)) {
		   return true;
		  } else {
		   return false;
		  }
	}

//   public void createPager(Context context, long EventID, ImageView imagev, ViewPager vImage, List<ImageView> imageList){
//        QueryBuilder<DBImage> qb = DBHelper.getInstance(context).getImageDao()
//                .queryBuilder().where(DBImageDao.Properties.EventID.eq(EventID));
//        if(qb.list().size() > 0) {
//            for(int i = 0;i<qb.list().size();i++){
//                Bitmap bm = ImageUtil.decodeSampledBitmapFromPath(qb.list().get(i).getImageUrl(), DensityUtil.dip2px(context, 96f),
//                        DensityUtil.dip2px(context, 96f));
//                LayoutInflater layoutInflater = LayoutInflater.from(context);
//                imagev = (ImageView)layoutInflater.inflate(R.layout.imageview_item,null);
//                imagev.setImageBitmap(bm);
//                imageList.add(imagev);
//            }
//            vImage.setCurrentItem(qb.list().size()*100);
//            vImage.setAdapter(new PagerAdapter() {
//                @Override
//                public int getCount() {
//                    if(imageList.size()==1){
//                        return 1;
//                    }
//                    return Integer.MAX_VALUE;
//                }
//
//                @Override
//                public boolean isViewFromObject(View view, Object o) {
//                    return view == o;
//                }
//
//                @Override
//                public Object instantiateItem(ViewGroup container, int position) {
//                    try{
//                        ((ViewPager)container).addView(imageList.get(position%imageList.size()),0);
//                    }catch (Exception e){
//                    }
//                    return imageList.get(position%imageList.size());
//                }
//
//                @Override
//                public int getItemPosition(Object object) {
//                    return super.getItemPosition(object);
//                }
//            });
//        }
//    }
	
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
	        int reqWidth, int reqHeight) {
		// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeResource(res, resId, options);
	    // 调用上面定义的方法计算inSampleSize值
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
	    // 使用获取到的inSampleSize值再次解析图片
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeResource(res, resId, options);
	}
	
	public static Bitmap decodeSampledBitmapFromPath(String path, int reqWidth, int reqHeight) {
		final BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, opt);
		opt.inSampleSize = calculateInSampleSize(opt, reqWidth, reqHeight);
		opt.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(path, opt);
	}
	
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// 源图片的高度和宽度
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			// 计算出实际宽高和目标宽高的比率
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			// 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
			// 一定都会大于等于目标的宽和高。
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}
	
	public static SpannableString getEventOtherDetail(DBEvent e, Context ctx) {
		List<ImageSpan> isList = new ArrayList<ImageSpan>();
		BitmapFactory.Options bo = new BitmapFactory.Options();
		bo.inJustDecodeBounds = true;
		Bitmap bm1 = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.ic_share, bo);
		int w = bo.outWidth;
		int nw = DensityUtil.dip2px(ctx, 12f);
		int scale = (int) (w / nw) + 2;
		if(scale == 0) {
			scale = 1;
		}
		bo.inSampleSize = scale;
		bo.inJustDecodeBounds = false;
		bm1 = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.ic_share, bo);
		ImageSpan is1 = new ImageSpan(ctx, bm1);
		isList.add(is1);
		
		bo.inJustDecodeBounds = true;
		Bitmap bm3 = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.ic_comment, bo);
		w = bo.outWidth;
		nw = DensityUtil.dip2px(ctx, 12f);
		scale = (int) (w / nw) + 2;
		if(scale == 0) {
			scale = 1;
		}
		bo.inSampleSize = scale;
		bo.inJustDecodeBounds = false;
		bm3 = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.ic_comment, bo);
		ImageSpan is3 = new ImageSpan(ctx, bm3);
		isList.add(is3);
		StringBuilder sb = new StringBuilder();
		long dateSpan = 0;
		if(e.getStarttime() == null) {
			sb.append("DEFAULT DATE | # ");
		} else {
			dateSpan = (e.getStarttime() - System.currentTimeMillis()) / (1000 * 60 * 60 * 24);
			if(dateSpan == 0) {
				sb.append("今天开始 | # ");
			} else if(dateSpan > 0) {
				sb.append(dateSpan).append("天后开始 | # ");
			} else {
				sb.append("已经结束了亲 | # ");
			}
		}
		
		int collectCount = e.getCollectionNum() == null ? 0 : e.getCollectionNum();
		int commentCount = e.getCommentNum() == null ? 0 : e.getCommentNum();
		sb.append(collectCount).append("人收藏 | # ")
			.append(commentCount).append("人评论");
		SpannableString ss = new SpannableString(sb.toString());
		for(int i = 0, j = 0; i < ss.length(); i++) if(ss.charAt(i) == '#') {
			ss.setSpan(isList.get(j++), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return ss;
	}
	
	public static SpannableString getEventOtherDetail(DBPickEvent e, Context ctx) {
		List<ImageSpan> isList = new ArrayList<ImageSpan>();
		BitmapFactory.Options bo = new BitmapFactory.Options();
		bo.inJustDecodeBounds = true;
		Bitmap bm1 = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.ic_share, bo);
		int w = bo.outWidth;
		int nw = DensityUtil.dip2px(ctx, 12f);
		int scale = (int) (w / nw) + 2;
		if(scale == 0) {
			scale = 1;
		}
		bo.inSampleSize = scale;
		bo.inJustDecodeBounds = false;
		bm1 = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.ic_share, bo);
		ImageSpan is1 = new ImageSpan(ctx, bm1);
		isList.add(is1);
		
		bo.inJustDecodeBounds = true;
		Bitmap bm3 = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.ic_comment, bo);
		w = bo.outWidth;
		nw = DensityUtil.dip2px(ctx, 12f);
		scale = (int) (w / nw) + 2;
		if(scale == 0) {
			scale = 1;
		}
		bo.inSampleSize = scale;
		bo.inJustDecodeBounds = false;
		bm3 = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.ic_comment, bo);
		ImageSpan is3 = new ImageSpan(ctx, bm3);
		isList.add(is3);
		StringBuilder sb = new StringBuilder();
		long dateSpan = 0;
		if(e.getStartd() == null) {
			sb.append("DEFAULT DATE | # ");
		} else {
			dateSpan = (parse(e.getStartd()).getTime() - System.currentTimeMillis()) / (1000 * 60 * 60 * 24);
			if(dateSpan == 0) {
				sb.append("今天开始 | # ");
			} else if(dateSpan > 0) {
				sb.append(dateSpan).append("天后开始 | # ");
			} else {
				sb.append("已经结束了亲 | # ");
			}
		}
		
		int collectCount = e.getCollectionNum() == null ? 0 : e.getCollectionNum();
		int commentCount = e.getCommentNum() == null ? 0 : e.getCommentNum();
		sb.append(collectCount).append("人收藏 | # ")
			.append(commentCount).append("人评论");
		SpannableString ss = new SpannableString(sb.toString());
		for(int i = 0, j = 0; i < ss.length(); i++) if(ss.charAt(i) == '#') {
			ss.setSpan(isList.get(j++), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return ss;
	}
}
