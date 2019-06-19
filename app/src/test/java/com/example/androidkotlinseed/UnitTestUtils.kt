package com.example.androidkotlinseed

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.example.androidkotlinseed.api.HeroListWrapper
import com.example.androidkotlinseed.injection.DaggerUnitTestApplicationComponent
import com.example.androidkotlinseed.injection.UnitTestApplicationComponent
import com.example.androidkotlinseed.injection.UnitTestApplicationModule
import com.example.androidkotlinseed.injection.UnitTestUseCaseModule
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import org.powermock.api.mockito.PowerMockito

class UnitTestUtils {

    fun <T> any(t: Class<out T>): T {
        return Mockito.any(t)
    }

    inline fun <reified T : Any> argumentCaptor() = ArgumentCaptor.forClass(T::class.java)

    fun getMockApp(): Application {
        val app: Application = PowerMockito.mock(Application::class.java)
        PowerMockito.`when`(app.applicationContext).thenReturn(app)

        return app
    }

    fun createTestApplicationComponent(): UnitTestApplicationComponent {
        return DaggerUnitTestApplicationComponent.builder()
            .applicationModule(UnitTestApplicationModule(getMockApp()))
            .useCaseModule(UnitTestUseCaseModule())
            .build()
    }

    fun createLifecycleOwner(): LifecycleRegistry {
        val lifeCycleOwner: LifecycleOwner = PowerMockito.mock(LifecycleOwner::class.java)
        return LifecycleRegistry(lifeCycleOwner)
    }

    private val mockHeroesJson = "{  \n" +
            "   \"superheroes\":[  \n" +
            "      {  \n" +
            "         \"name\":\"Spiderman\",\n" +
            "         \"photo\":\"https://i.annihil.us/u/prod/marvel/i/mg/9/30/538cd33e15ab7/standard_xlarge.jpg\",\n" +
            "         \"realName\":\"Peter Benjamin Parker\",\n" +
            "         \"height\":\"1.77m\",\n" +
            "         \"power\":\"Peter can cling to most surfaces, has superhuman strength (able to lift 10 tons optimally) and is roughly 15 times more agile than a regular human.\",\n" +
            "         \"abilities\":\"Peter is an accomplished scientist, inventor and photographer.\",\n" +
            "         \"groups\":\"Avengers, formerly the Secret Defenders, \\\"New Fantastic Four\\\", the Outlaws\"\n" +
            "      },\n" +
            "      {  \n" +
            "         \"name\":\"Captain Marvel\",\n" +
            "         \"photo\":\"https://i.annihil.us/u/prod/marvel/i/mg/c/10/537ba5ff07aa4/standard_xlarge.jpg\",\n" +
            "         \"realName\":\"Carol Danvers\",\n" +
            "         \"height\":\"1.80m\",\n" +
            "         \"power\":\"Ms. Marvel's current powers include flight, enhanced strength, durability and the ability to shoot concussive energy bursts from her hands.\",\n" +
            "         \"abilities\":\"Ms. Marvel is a skilled pilot \\u0026 hand to hand combatant\",\n" +
            "         \"groups\":\"Avengers, formerly Queen's Vengeance, Starjammers\"\n" +
            "      },\n" +
            "      {  \n" +
            "         \"name\":\"Hulk\",\n" +
            "         \"photo\":\"https://i.annihil.us/u/prod/marvel/i/mg/5/a0/538615ca33ab0/standard_xlarge.jpg\",\n" +
            "         \"realName\":\"Robert Bruce Banner\",\n" +
            "         \"height\":\"1.75m\",\n" +
            "         \"power\":\"The Hulk possesses an incredible level of superhuman physical ability.\",\n" +
            "         \"abilities\":\"Dr. Bruce Banner is a genius in nuclear physics, possessing a mind so brilliant that it cannot be measured on any known intelligence test. When Banner is the Hulk, Banner's consciousness is buried within the Hulk's, and can influence the Hulk's behavior only to a very limited extent.\",\n" +
            "         \"groups\":\"Formerly Avengers, Defenders, Fantastic Four, Pantheon, Horsemen of Apocalypse, Warbound\"\n" +
            "      },\n" +
            "      {  \n" +
            "         \"name\":\"Thor\",\n" +
            "         \"photo\":\"https://i.annihil.us/u/prod/marvel/i/mg/5/a0/537bc7036ab02/standard_xlarge.jpg\",\n" +
            "         \"realName\":\"Thor Odinson\",\n" +
            "         \"height\":\"1.98m\",\n" +
            "         \"power\":\"As the son of Odin and Gaea, Thor's strength, endurance and resistance to injury are greater than the vast majority of his superhuman race.\",\n" +
            "         \"abilities\":\"Thor is trained in the arts of war, being a superbly skilled warrior, highly proficient in hand-to-hand combat, swordsmanship and hammer throwing.\",\n" +
            "         \"groups\":\"Gods of Asgard, Avengers; formerly Queen’s Vengeance, Godpack, Thor Corps\"\n" +
            "      },\n" +
            "      {  \n" +
            "         \"name\":\"Iron Man\",\n" +
            "         \"photo\":\"https://i.annihil.us/u/prod/marvel/i/mg/6/a0/55b6a25e654e6/standard_xlarge.jpg\",\n" +
            "         \"realName\":\"Anthony Edward \\\"Tony\\\" Stark\",\n" +
            "         \"height\":\"1.85m\",\n" +
            "         \"power\":\"None; Tony's body had been enhanced by the modified techno-organic virus, Extremis, but it is currently inaccessible and inoperable.\",\n" +
            "         \"abilities\":\"Tony has a genius level intellect that allows him to invent a wide range of sophisticated devices, specializing in advanced weapons and armor. He possesses a keen business mind.\",\n" +
            "         \"groups\":\"The Avengers, Initiative, Hellfire Club (outer circle), S.H.I.E.L.D., Illuminati, Thunderbolts, Force Works, Queen's Vengeance, Alcoholics Anonymous\"\n" +
            "      },\n" +
            "      {  \n" +
            "         \"name\":\"Captain America\",\n" +
            "         \"photo\":\"https://i.annihil.us/u/prod/marvel/i/mg/3/50/537ba56d31087/standard_xlarge.jpg\",\n" +
            "         \"realName\":\"Steven \\\"Steve\\\" Rogers\",\n" +
            "         \"height\":\"1.87m\",\n" +
            "         \"power\":\"The Super-Soldier formula that he had metabolized had enhanced all of his bodily functions to the peak of human efficiency. Most notably, his body eliminates the excessive build-up of fatigue-producing poisons in his muscles, granting him phenomenal endurance.\",\n" +
            "         \"abilities\":\"Captain America had mastered the martial arts of American-style boxing and judo, and had combined these disciplines with his own unique hand-to-hand style of combat.\",\n" +
            "         \"groups\":\"Secret Avengers; formerly the Avengers, Invaders, Captain's Unnamed Superhero Team, Redeemers; formerly partner of Winter Soldier, Bucky, Jones, Rick, Rick Jones, Falcon (Sam Wilson), Falcon, Demolition Man and Nomad (Jack Monroe)\"\n" +
            "      }\n" +
            "   ]\n" +
            "}"

    val heroListWrapper = HeroListWrapper.fromJson(mockHeroesJson)
}