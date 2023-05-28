package com.blinkslabs.blinkist.android.challenge.data.api

import com.blinkslabs.blinkist.android.challenge.data.model.Book
import kotlinx.coroutines.delay
import org.threeten.bp.LocalDate
import javax.inject.Inject

class MockBooksApi @Inject constructor() : BooksApi {

    override suspend fun getAllBooks(): List<Book> {
        delay(2000)
        return listOf(
            Book(
                "d241b2b",
                "Eat, Move, Sleep",
                "Tom Rath",
                LocalDate.of(2018, 7, 3),
                "https://firebasestorage.googleapis.com/v0/b/joniaranguri-resume.appspot.com/o/code-challenge%2Feat-move-sleep.png?alt=media&token=96223387-d7e1-487f-bbf8-4e0e71389187"
            ),
            Book(
                "eea5ee1",
                "The Secret Life of Sleep",
                "Kat Duff",
                LocalDate.of(2018, 7, 2),
                "https://firebasestorage.googleapis.com/v0/b/joniaranguri-resume.appspot.com/o/code-challenge%2Fthe-secret-life-os.png?alt=media&token=41ec03e5-2489-4aea-b90c-c0c4df4ab93e"
            ),
            Book(
                "7e2401d",
                "The Sleep Revolution",
                "Arianna Huffington",
                LocalDate.of(2018, 6, 19),
                "https://firebasestorage.googleapis.com/v0/b/joniaranguri-resume.appspot.com/o/code-challenge%2Fthe-sleep-revolution.png?alt=media&token=781aa1ba-2b76-4669-8125-42b456e0eb39"
            ),
            Book(
                "03779ee",
                "Real Artists Don’t Starve",
                "Jeff Goins",
                LocalDate.of(2017, 12, 31),
                "https://firebasestorage.googleapis.com/v0/b/joniaranguri-resume.appspot.com/o/code-challenge%2Freal-artist-dns.png?alt=media&token=0289f5bd-081b-4a21-8593-7cfa8c82d3d2"
            ),
            Book(
                "e021f6c",
                "Hirntuning",
                "Dave Asprey",
                LocalDate.of(2018, 1, 1),
                "https://firebasestorage.googleapis.com/v0/b/joniaranguri-resume.appspot.com/o/code-challenge%2Fhirn-tunning.png?alt=media&token=e9da89c5-35ad-4d86-9d33-2119f89a6552"
            ),
            Book(
                "8722651",
                "The Red Queen",
                "Matt Ridley",
                LocalDate.of(2018, 6, 17),
                "https://firebasestorage.googleapis.com/v0/b/joniaranguri-resume.appspot.com/o/code-challenge%2Fthe-red-queen.png?alt=media&token=be10f422-6f30-4965-8db2-67fec73d12f0"
            ),
            Book(
                "2cb8609",
                "Inner Engineering",
                "Sadhguru Jaggi Vasudev",
                LocalDate.of(2018, 6, 18),
                "https://firebasestorage.googleapis.com/v0/b/joniaranguri-resume.appspot.com/o/code-challenge%2Finner-engineering.png?alt=media&token=92c4b165-014a-4577-adb1-24131d6e397b"
            ),
            Book(
                "b4388e4",
                "Feathers",
                "Thor Hanson",
                LocalDate.of(2018, 6, 18),
                "https://firebasestorage.googleapis.com/v0/b/joniaranguri-resume.appspot.com/o/code-challenge%2Ffeathers.png?alt=media&token=120fe166-e6e7-43d4-bba7-c00ea2d31902"
            ),
            Book(
                "1cdb347",
                "The Subtle Art of Not Giving a F*ck",
                "Mark Manson",
                LocalDate.of(2016, 7, 2),
                "https://firebasestorage.googleapis.com/v0/b/joniaranguri-resume.appspot.com/o/code-challenge%2Fthe-subtle-art-of-ngaf.png?alt=media&token=a2ff2e8a-0e77-4c31-85ec-df5115c56076"
            ),
            Book(
                "a597717",
                "Bringing Up Bébé",
                "Pamela Druckerman",
                LocalDate.of(2016, 7, 3),
                "https://firebasestorage.googleapis.com/v0/b/joniaranguri-resume.appspot.com/o/code-challenge%2Fbringing-up-bebe.png?alt=media&token=fe578bd3-630a-4b6d-bec6-9db50199062a"
            ),
            Book(
                "99c1c39",
                "A Book With a Very Long Title, Veeeeeeeeeeeeeeeeery Long, Possibly the Most Long Title For a Book You've Ever Seen In Your Entire Life",
                "The Blinkist Android Team",
                LocalDate.of(2014, 1, 1),
                "https://firebasestorage.googleapis.com/v0/b/joniaranguri-resume.appspot.com/o/code-challenge%2Fthe-entrepreneur-rc.png?alt=media&token=894d2667-cc75-4c4c-868b-02bca1570745"
            )
        )
    }
}
