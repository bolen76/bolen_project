import org.junit.Test;

import com.bolen.seckill.common.constant.OrderStatus;

public class EnumTest {

	@Test
	public void test01() {
		OrderStatus invalid = OrderStatus.PAYED;

		System.out.println(invalid.ordinal());
		System.out.println(invalid.toString());
		System.out.println(invalid.getStatus());
	}

	public static void main(String[] args) {
		Size s = Enum.valueOf(Size.class, "SMALL");

		System.out.println(s);
		String string = s.toString();
		System.out.println(string);
		String abbreviation = s.getAbbreviation();
		System.out.println(abbreviation);
		String abbreviation2 = Size.LARGE.getAbbreviation();
		System.out.println(abbreviation2);

	}

	enum Size {

		SMALL("hello"), MEDIUM("123"), LARGE("JAVA");

		private String abbreviation;

		private Size(String abbreviation) {
			this.abbreviation = abbreviation;
		}

		public String getAbbreviation() {
			return abbreviation;
		}

	}
}
