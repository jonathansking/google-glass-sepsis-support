package edu.ucdavis.glass.sepsis.support;

interface AsyncTaskCompleteListener<T> 
{
	   public void onTaskComplete(T result);
}