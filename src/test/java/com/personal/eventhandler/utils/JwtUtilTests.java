package com.personal.eventhandler.utils;

import org.junit.jupiter.api.Test;
import java.time.Clock;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class JwtUtilTests {

    @Test
    public void getWeatherTokenTest() {
        Clock clock = Clock.systemUTC();
        String issuer = "issuer";
        String secret = "tnumklcfilemyrjbzugwsmctnrumqvmavgenkqrzgxphvhceytqelrpcgaxrnkthhxxdubwipqjsqppfkbvdphnirzaobjghwljcsqtnbdihqyryjlsqzrcephqpodqrdaezlaueuywwbacvrybggszehfvjxiuepmkeaeyrbiqpxxenofxyevuyewuookvumqwlobqmjqzczftsvspjijeenylsdlpknhbolvskvghhxwxcvavxtgvjjazdlyblikomizftoxojakmjzaesflskpyjdxksammaqxdvuoyribtdsrlbpgxysgyjncxwicxdzvawbgpcitxwqdgabvcdlqiddrdwadsxosjffjklechwbdvngfkdmboslluldpmlctmvtugteowqurrvlnntynrgoxjcetlgekstiytgmcwfsfrpyfbyvpezhsohcionvjgftkxytpzmixboqagvcrstfgsxmxtfnaxjghshnxhddpxcokbfsoxfjhuww";

        JwtUtil underTest = new JwtUtil(clock, issuer, secret);

        String result = underTest.getWeatherToken();
        assertTrue(result.startsWith("Bearer"));
    }
}
