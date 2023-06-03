// Testing sequences of 10 commands.
import junit.framework.TestCase;

import edu.uwm.cs351.Duration;
import edu.uwm.cs351.Period;
import edu.uwm.cs351.Time;
import edu.uwm.cs351.Appointment;
import edu.uwm.cs351.ApptBook;


public class TestGen extends TestCase {
	protected void assertException(Class<?> exc, Runnable r) {
		try {
			r.run();
			assertFalse("should have thrown an exception.",true);
		} catch (RuntimeException e) {
			if (exc == null) return;
			assertTrue("threw wrong exception type: " + e.getClass(),exc.isInstance(e));
		}
	}

	protected void assertEquals(int expected, Integer result) {
		super.assertEquals(Integer.valueOf(expected),result);
	}

	Time now = new Time();
	Appointment[] p = new Appointment[]{ null,
		new Appointment(new Period(now,Duration.HOUR.scale(1)),"1"),
		new Appointment(new Period(now,Duration.HOUR.scale(2)),"2"),
		new Appointment(new Period(now,Duration.HOUR.scale(3)),"3"),
		new Appointment(new Period(now,Duration.HOUR.scale(4)),"4"),
		new Appointment(new Period(now,Duration.HOUR.scale(5)),"5"),
		new Appointment(new Period(now,Duration.HOUR.scale(6)),"6"),
		new Appointment(new Period(now,Duration.HOUR.scale(7)),"7"),
		new Appointment(new Period(now,Duration.HOUR.scale(8)),"8"),
		new Appointment(new Period(now,Duration.HOUR.scale(9)),"9"),
	};

	public void test() {
		ApptBook ps0 = new ApptBook();
		assertException(java.lang.NullPointerException.class, () -> ps0.setCurrent(null));
	}
}
