package com.it.entity;

import java.util.Collections;
import java.util.List;

public class PageResult<T>
{
	private List<T> records = Collections.emptyList();
	private int total = 0;
	private int size = 10;
	private int current = 1;

	public boolean hasPrevious()
	{
		return this.current > 1;
	}

	public boolean hasNext()
	{
		return this.current < this.getPages();
	}

	private int getPages()
	{
		if (this.getSize() == 0)
		{
			return 0;
		}
		else
		{
			int pages = this.getTotal() / this.getSize();
			if (this.getTotal() % this.getSize() != 0)
			{
				++pages;
			}
			return pages;
		}
	}

	public PageResult()
	{

	}

	public List<T> getRecords()
	{
		return records;
	}

	public void setRecords(List<T> records)
	{
		this.records = records;
	}

	public int getTotal()
	{
		return total;
	}

	public void setTotal(int total)
	{
		this.total = total;
	}

	public int getSize()
	{
		return size;
	}

	public void setSize(int size)
	{
		this.size = size;
	}

	public int getCurrent()
	{
		return current;
	}

	public void setCurrent(int current)
	{
		this.current = current;
	}

	@Override
	public String toString()
	{
		return "PageResult{" + "records=" + records + ", total=" + total + ", size=" + size + ", current=" + current + '}';
	}
}
