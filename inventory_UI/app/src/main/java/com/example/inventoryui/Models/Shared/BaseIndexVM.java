package com.example.inventoryui.Models.Shared;

import android.util.Log;

import com.example.inventoryui.Annotations.DropDownAnnotation;
import com.example.inventoryui.Annotations.EnumAnnotation;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

public class BaseIndexVM<E extends BaseModel, F extends BaseFilterVM ,O extends BaseOrderBy> implements Serializable
{
	//final String TAG="MyActivity_baseMain";
	final String TAG="MyActivity_BaseIndexVM";
	private PagerVM Pager ;
	private F Filter ;
	private O OrderBy ;
	private List<E> Items ;

	@JsonIgnore
	public String getUrl() {
		StringBuilder sb = new StringBuilder();
		if(this.Pager!=null)
			getUrl( sb, Pager , Pager.getPrefix());
		if( this.OrderBy != null)
		{ }//sb.append("&"); getUrl( sb, this.OrderBy , this.OrderBy.getPrefix()); }
		if(this.Filter != null)
		{ sb.append("&"); getUrl( sb, this.Filter , this.Filter.getPrefix()); }
		if(sb.length()>0)
			sb.insert(0,"?");
		Log.i(TAG,"this class = "+this.getClass().getName());
		Log.i(TAG,"url = "+sb.toString());
		return sb.toString();

	}

	private String getUrl(StringBuilder sb, Object obj, String prefix){
		try {
			for (Field f : obj.getClass().getDeclaredFields()) {
				Annotation[] annotations = f.getDeclaredAnnotations();
				Log.i(TAG,"f.name in get url = "+(f.getName()));
				Log.i(TAG,"annotations == null in get url = "+(annotations==null));
				boolean skip = false;
				for (Annotation annotation : annotations) {
					if (annotation instanceof DropDownAnnotation || annotation instanceof EnumAnnotation) skip = true;
				}
				if(!skip) {
					f.setAccessible(true);
					if (f.get(obj) == null || f.getName().equals("Prefix")) {
						continue;
					}
					sb.append(prefix);
					sb.append(".");
					sb.append(f.getName());
					sb.append("=");
					if (f.getType().equals(List.class)) {
						String listToString = f.get(obj).toString();
						listToString = (listToString.substring(1, listToString.length() - 1))
								.replaceAll("\\s", "");//replace white spaces
						sb.append(listToString);
					} else sb.append(f.get(obj));
					sb.append("&");
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		if(sb.length() > 0){
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	public BaseIndexVM() {	}

	public BaseIndexVM(PagerVM pager, F filter, O orderBy, String hello, List<E> items) {
		Pager = pager;
		Filter = filter;
		OrderBy = orderBy;
		Items = items;
	}

	public PagerVM getPager() {
		return Pager;
	}

	public void setPager(PagerVM pager) {
		Pager = pager;
	}

	public F getFilter() {
		return Filter;
	}

	public void setFilter(F filter) {
		Filter = filter;
	}

	public O getOrderBy() {
		return OrderBy;
	}

	public void setOrderBy(O orderBy) {
		OrderBy = orderBy;
	}

	public List<E> getItems() {
		return Items;
	}

	public void setItems(List<E> items) {
		Items = items;
	}

}
