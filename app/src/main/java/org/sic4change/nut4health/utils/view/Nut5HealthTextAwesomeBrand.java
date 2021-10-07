package org.sic4change.nut4health.utils.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.LruCache;

import androidx.appcompat.widget.AppCompatTextView;


public class Nut5HealthTextAwesomeBrand extends AppCompatTextView {

	private final static String NAME = "FONTAWESOME";
	private static LruCache<String, Typeface> sTypefaceCache = new LruCache<String, Typeface>(12);

	public Nut5HealthTextAwesomeBrand(Context context) {
		super(context);
		init();

	}

	public Nut5HealthTextAwesomeBrand(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public void init() {

		Typeface typeface = sTypefaceCache.get(NAME);

		if (typeface == null) {

			typeface = Typeface.createFromAsset(getContext().getAssets(), "brandsawesomefont.ttf");
			sTypefaceCache.put(NAME, typeface);

		}

		setTypeface(typeface);

	}

}


