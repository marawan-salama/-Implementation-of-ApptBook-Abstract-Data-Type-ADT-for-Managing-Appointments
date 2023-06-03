// This is an assignment for students to complete after reading Chapter 3 of
// "Data Structures and Other Objects Using Java" by Michael Main.

package edu.uwm.cs351;

import junit.framework.TestCase;

/******************************************************************************
 * An ApptBook ("book" for short) is a sequence of Appointment objects in sorted
 * order. The book can have a special "current element," which is specified and
 * accessed through four methods that are available in the this class (start,
 * getCurrent, advance and isCurrent).
 * <p>
 * Notes:
 * <ol>
 * <li>The capacity of a book can change after it's created, but the maximum
 * capacity is limited by the amount of free memory on the machine. The
 * constructor, insert, insertAll, and clone methods will result in an
 * {@link java.lang.OutOfMemoryError} when free memory is exhausted.
 * <li>A book's capacity cannot exceed the maximum integer 2,147,483,647
 * ({@link Integer#MAX_VALUE}). Any attempt to create a larger capacity results
 * in a failure due to an arithmetic overflow.
 * </ol>
 * <b>NB</b>: Neither of these conditions require any work for the implementors
 * (students).
 *
 *
 ******************************************************************************/
public class ApptBook implements Cloneable {

	/** Static Constants */
	private static final int INITIAL_CAPACITY = 1;

	/** Fields */
	private Appointment[] data;
	private int manyItems;
	private int currentIndex;

	private static boolean doReport = true; // change only in invariant tests

	private boolean report(String error) {
		if (doReport) {
			System.out.println("Invariant error: " + error);
		}
		return false;
	}

	// Invariant of the ApptBook class:
	// 1. The number of elements in the books is in the instance variable
	// manyItems.
	// 2. For an empty book (with no elements), we do not care what is
	// stored in any of data; for a non-empty book, the elements of the
	// book are stored in data[0] through data[manyItems-1] and we
	// don't care what's in the rest of data.
	// 3. None of the elements are null and they are ordered according to their
	// natural ordering (Comparable); duplicates *are* allowed.
	// 4. If there is a current element, then it lies in data[currentIndex];
	// if there is no current element, then currentIndex equals manyItems.

	private boolean wellFormed() {
		if (data == null)
			return report(" data is null");
		if (data.length < manyItems)
			return report(" data is too short");
		for (int i = 0; i < manyItems; ++i) {
			if (data[i] == null)
				return report("null found at index " + i);
			if (i > 0 && data[i - 1].compareTo(data[i]) > 0)
				return report("Found out of order data");
		}
		if (currentIndex < 0 || currentIndex > manyItems)
			return report("currentIndex is out of range");
		return true;
	}

	// This is only for testing the invariant.
	private ApptBook(boolean testInvariant) {
	}

	/**
	 * Initialize an empty book with an initial capacity of INITIAL_CAPACITY. The
	 * {@link #insert(Appointment)} method works efficiently (without needing more
	 * memory) until this capacity is reached.
	 * 
	 * @postcondition This book is empty and has an initial capacity of
	 *                INITIAL_CAPACITY
	 * @exception OutOfMemoryError Indicates insufficient memory for initial array.
	 **/
	public ApptBook() {
		this(INITIAL_CAPACITY);
		assert wellFormed() : "invariant failed at end of constructor";
	}

	/**
	 * Initialize an empty book with a specified initial capacity. The
	 * {@link #insert(Appointment)} method works efficiently (without needing more
	 * memory) until this capacity is reached.
	 * 
	 * @param initialCapacity the initial capacity of this book, must not be
	 *                        negative
	 * @exception IllegalArgumentException Indicates that initialCapacity is invalid
	 * @exception OutOfMemoryError         Indicates insufficient memory for an
	 *                                     array with this many elements. new
	 *                                     Appointment[initialCapacity].
	 **/
	public ApptBook(int initialCapacity) {
		if (initialCapacity < 0)
			throw new IllegalArgumentException();
		data = new Appointment[initialCapacity];
		manyItems = currentIndex = 0;
		assert wellFormed() : "invariant failed at end of constructor";
	}

	/**
	 * Determine the number of elements in this book.
	 * 
	 * @return the number of elements in this book
	 **/
	public int size() {
		assert wellFormed() : "invariant failed at start of size";
		return manyItems;
	}

	/**
	 * Set the current element at the front of this book.
	 * 
	 * @postcondition The front element of this book is now the current element (but
	 *                if this book has no elements at all, then there is no current
	 *                element).
	 **/
	public void start() {
		assert wellFormed() : "invariant failed at start of start";
		currentIndex = 0;
		assert wellFormed() : "invariant failed at end of start";
	}

	/**
	 * Accessor method to determine whether this book has a specified current
	 * element that can be retrieved with the getCurrent method.
	 * 
	 * @return true (there is a current element) or false (there is no current
	 *         element at the moment)
	 **/
	public boolean isCurrent() {
		assert wellFormed() : "invariant failed at start of isCurrent";
		return currentIndex < manyItems;
	}

	/**
	 * Accessor method to get the current element of this book.
	 * 
	 * @precondition isCurrent() returns true.
	 * @return the current element of this book
	 * @exception IllegalStateException Indicates that there is no current element,
	 *                                  so getCurrent may not be called.
	 **/
	public Appointment getCurrent() {
		assert wellFormed() : "invariant failed at start of getCurrent";
		if (!isCurrent())
			throw new IllegalStateException("no current");
		return data[currentIndex];
	}

	/**
	 * Move forward, so that the current element will be the next element in this
	 * book.
	 * 
	 * @precondition isCurrent() returns true.
	 * @postcondition If the current element was already the end element of this
	 *                book (with nothing after it), then there is no longer any
	 *                current element. Otherwise, the new element is the element
	 *                immediately after the original current element.
	 * @exception IllegalStateException Indicates that there is no current element,
	 *                                  so advance may not be called.
	 **/
	public void advance() {
		assert wellFormed() : "invariant failed at start of advance";
		if (manyItems <= currentIndex)
			throw new IllegalStateException("advancing past end");
		++currentIndex;
		assert wellFormed() : "invariant failed at end of advance";
	}

	/**
	 * Remove the current element from this book.
	 * 
	 * @precondition isCurrent() returns true.
	 * @postcondition The current element has been removed from this book, and the
	 *                following element (if there is one) is now the new current
	 *                element. If there was no following element, then there is now
	 *                no current element.
	 * @exception IllegalStateException Indicates that there is no current element,
	 *                                  so removeCurrent may not be called.
	 **/
	public void removeCurrent() {
		assert wellFormed() : "invariant failed at start of removeCurrent";
		if (!isCurrent())
			throw new IllegalStateException("no current to remove");
		--manyItems;
		for (int i = currentIndex; i < manyItems; ++i) {
			data[i] = data[i + 1];
		}
		assert wellFormed() : "invariant failed at end of removeCurrent";
	}

	/**
	 * Set the current element to the first element that is equal or greater than
	 * the guide.
	 * 
	 * @param guide element to compare against, must not be null.
	 */
	/**
	 * @exception NullPointerException Indicates that addend is null.
	 */
	public void setCurrent(Appointment guide) {
		assert wellFormed() : "invariant failed at start of setCurrent";
		if (guide == null)
			throw new NullPointerException(" guide cannot be null");
		start();
		while (isCurrent() && getCurrent().compareTo(guide) < 0) {
			advance();
		}
		assert wellFormed() : "invariant failed at end of setCurrent";
	}

	/**
	 * Change the current capacity of this book as needed so that the capacity is at
	 * least as big as the parameter. This code must work correctly and efficiently
	 * if the minimum capacity is (1) smaller or equal to the current capacity (do
	 * nothing) (2) at most double the current capacity (double the capacity) or (3)
	 * more than double the current capacity (new capacity is the minimum passed).
	 * 
	 * @param minimumCapacity the new capacity for this book
	 * @postcondition This book's capacity has been changed to at least
	 *                minimumCapacity. If the capacity was already at or greater
	 *                than minimumCapacity, then the capacity is left unchanged.
	 * @exception OutOfMemoryError Indicates insufficient memory for: new array of
	 *                             minimumCapacity elements.
	 **/
	private void ensureCapacity(int minimumCapacity) {
		if (minimumCapacity <= data.length)
			return;
		int newCap = data.length * 2;
		if (newCap < minimumCapacity)
			newCap = minimumCapacity;
		Appointment[] newData = new Appointment[newCap];
		for (int i = 0; i < manyItems; ++i) {
			newData[i] = data[i];
		}
		data = newData;
		return;
	}

	/**
	 * Add a new element to this book, in order. If an equal appointment is already
	 * in the book, it is inserted after the last of these. If the new element would
	 * take this book beyond its current capacity, then the capacity is increased
	 * before adding the new element. The current element (if any) is not affected.
	 * 
	 * @param element the new element that is being added, must not be null
	 * @postcondition A new copy of the element has been added to this book. The
	 *                current element (whether or not is exists) is not changed.
	 * @exception IllegalArgumentException indicates the parameter is null
	 * @exception OutOfMemoryError         Indicates insufficient memory for
	 *                                     increasing the book's capacity.
	 **/
	public void insert(Appointment element) {
		assert wellFormed() : "invariant failed at start of insert";
		if (element == null)
			throw new IllegalArgumentException("cannot insert null");
		ensureCapacity(manyItems + 1);
		int i;
		for (i = manyItems; i > 0 && data[i - 1].compareTo(element) > 0; --i) {
			data[i] = data[i - 1];
		}
		data[i] = element;
		if (currentIndex >= i)
			++currentIndex;
		++manyItems;
		assert wellFormed() : "invariant failed at end of insert";
	}

	/**
	 * Place all the appointments of another book (which may be the same book as
	 * this!) into this book in order as in {@link #insert}. The elements should
	 * added one by one from the start. The elements are probably not going to be
	 * placed in a single block.
	 * 
	 * @param addend a book whose contents will be placed into this book
	 * @precondition The parameter, addend, is not null.
	 * @postcondition The elements from addend have been placed into this book. The
	 *                current element (if any) is unchanged.
	 * @exception NullPointerException Indicates that addend is null.
	 * @exception OutOfMemoryError     Indicates insufficient memory to increase the
	 *                                 size of this book.
	 **/
	public void insertAll(ApptBook addend) {
		assert wellFormed() : "invariant failed at start of insertAll";
		if (addend.manyItems == 0)
			return;
		;
		ensureCapacity(manyItems + addend.manyItems);
		if (addend == this)
			addend = addend.clone();
		for (int i = 0; i < addend.manyItems; ++i) {
			insert(addend.data[i]);
		}
		assert wellFormed() : "invariant failed at end of insertAll";
		assert addend.wellFormed() : "invariant of addend broken in insertAll";
	}

	/**
	 * Generate a copy of this book.
	 * 
	 * @return The return value is a copy of this book. Subsequent changes to the
	 *         copy will not affect the original, nor vice versa.
	 * @exception OutOfMemoryError Indicates insufficient memory for creating the
	 *                             clone.
	 **/
	public ApptBook clone() {
		assert wellFormed() : "invariant failed at start of clone";
		ApptBook answer;

		try {
			answer = (ApptBook) super.clone();
		} catch (CloneNotSupportedException e) { // This exception should not occur. But if it does, it would probably
													// indicate a programming error that made super.clone unavailable.
													// The most common error would be forgetting the "Implements
													// Cloneable"
													// clause at the start of this class.
			throw new RuntimeException("This class does not implement Cloneable");
		}

		// all that is needed is to clone the data array.
		// (Exercise: Why is this needed?)
		answer.data = data.clone();

		assert wellFormed() : "invariant failed at end of clone";
		assert answer.wellFormed() : "invariant on answer failed at end of clone";
		return answer;
	}

	// don't change this nested class:
	public static class TestInvariantChecker extends TestCase {
		Time now = new Time();
		Appointment e1 = new Appointment(new Period(now, Duration.HOUR), "1: think");
		Appointment e2 = new Appointment(new Period(now, Duration.DAY), "2: current");
		Appointment e3 = new Appointment(new Period(now.add(Duration.HOUR), Duration.HOUR), "3: eat");
		Appointment e4 = new Appointment(new Period(now.add(Duration.HOUR.scale(2)), Duration.HOUR.scale(8)),
				"4: sleep");
		Appointment e5 = new Appointment(new Period(now.add(Duration.DAY), Duration.DAY), "5: tomorrow");
		ApptBook hs;

		protected void setUp() {
			hs = new ApptBook(false);
			ApptBook.doReport = false;
		}

		public void test0() {
			assertFalse(hs.wellFormed());
		}

		public void test1() {
			hs.data = new Appointment[0];
			hs.manyItems = -1;
			assertFalse(hs.wellFormed());
			hs.manyItems = 1;
			assertFalse(hs.wellFormed());

			doReport = true;
			hs.manyItems = 0;
			assertTrue(hs.wellFormed());
		}

		public void test2() {
			hs.data = new Appointment[1];
			hs.manyItems = 1;
			assertFalse(hs.wellFormed());
			hs.manyItems = 2;
			assertFalse(hs.wellFormed());
			hs.data[0] = e1;

			doReport = true;
			hs.manyItems = 0;
			assertTrue(hs.wellFormed());
			hs.manyItems = 1;
			hs.data[0] = e1;
			assertTrue(hs.wellFormed());
		}

		public void test3() {
			hs.data = new Appointment[3];
			hs.manyItems = 2;
			hs.data[0] = e2;
			hs.data[1] = e1;
			assertFalse(hs.wellFormed());

			doReport = true;
			hs.data[0] = e1;
			assertTrue(hs.wellFormed());
			hs.data[1] = e2;
			assertTrue(hs.wellFormed());
		}

		public void test4() {
			hs.data = new Appointment[10];
			hs.manyItems = 3;
			hs.data[0] = e2;
			hs.data[1] = e4;
			hs.data[2] = e3;
			assertFalse(hs.wellFormed());

			doReport = true;
			hs.data[2] = e5;
			assertTrue(hs.wellFormed());
			hs.data[0] = e4;
			assertTrue(hs.wellFormed());
			hs.data[1] = e5;
			assertTrue(hs.wellFormed());
		}

		public void test5() {
			hs.data = new Appointment[10];
			hs.manyItems = 4;
			hs.data[0] = e1;
			hs.data[1] = e3;
			hs.data[2] = e2;
			hs.data[3] = e5;
			assertFalse(hs.wellFormed());

			doReport = true;
			hs.data[2] = e4;
			assertTrue(hs.wellFormed());
			hs.data[2] = e5;
			assertTrue(hs.wellFormed());
			hs.data[2] = e3;
			assertTrue(hs.wellFormed());
		}

		public void test6() {
			hs.data = new Appointment[3];
			hs.manyItems = -2;
			assertFalse(hs.wellFormed());
			hs.manyItems = -1;
			assertFalse(hs.wellFormed());
			hs.manyItems = 1;
			assertFalse(hs.wellFormed());

			doReport = true;
			hs.manyItems = 0;
			assertTrue(hs.wellFormed());
		}

		public void test7() {
			hs.data = new Appointment[3];
			hs.data[0] = e1;
			hs.data[1] = e2;
			hs.data[2] = e4;
			hs.manyItems = 4;
			assertFalse(hs.wellFormed());

			doReport = true;
			hs.manyItems = 3;
			assertTrue(hs.wellFormed());
		}

		public void test8() {
			hs.data = new Appointment[3];
			hs.data[0] = e2;
			hs.data[1] = e3;
			hs.data[2] = e1;
			hs.manyItems = 2;
			hs.currentIndex = -1;
			assertFalse(hs.wellFormed());
			hs.currentIndex = 3;
			assertFalse(hs.wellFormed());

			doReport = true;
			hs.currentIndex = 0;
			assertTrue(hs.wellFormed());
			hs.currentIndex = 1;
			assertTrue(hs.wellFormed());
			hs.currentIndex = 2;
			assertTrue(hs.wellFormed());
		}
	}
}
