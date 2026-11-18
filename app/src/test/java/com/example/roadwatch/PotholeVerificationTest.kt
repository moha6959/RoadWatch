package com.example.roadwatch

import com.example.roadwatch.ui.scan.Detection
import com.example.roadwatch.ui.scan.Verdict
import com.example.roadwatch.ui.scan.confirmedDetections
import com.example.roadwatch.ui.scan.liveVerificationSummary
import org.junit.Assert.assertEquals
import org.junit.Test

class PotholeVerificationTest {
    private val boxes = List(4) { Detection(it / 10f, 0.1f, 0.1f, 0.1f, 0.8f) }

    @Test fun savesOnlyConfirmedBoxesAndPreservesTheirOrder() {
        val results = confirmedDetections(boxes, listOf(Verdict.CONFIRMED, Verdict.REJECTED, Verdict.CONFIRMED))
        assertEquals(listOf(boxes[0], boxes[2]), results)
    }

    @Test fun discardsDetectionsWhenVerificationIsUnavailable() {
        assertEquals(emptyList<Detection>(), confirmedDetections(boxes, emptyList()))
    }

    @Test fun discardsAFrameWithOnlyRejectedUncertainOrUncheckedBoxes() {
        val results = confirmedDetections(boxes, listOf(Verdict.REJECTED, Verdict.UNCERTAIN))
        assertEquals(emptyList<Detection>(), results)
    }
    @Test fun displaysMixedGeminiVerdicts() {
        assertEquals(
            "Gemini confirmed 1 · rejected 1 · unsure about 1",
            liveVerificationSummary(listOf(Verdict.CONFIRMED, Verdict.REJECTED, Verdict.UNCERTAIN))
        )
    }

    @Test fun distinguishesUnavailableVerificationFromRejection() {
        assertEquals("Gemini verification unavailable", liveVerificationSummary(emptyList()))
        assertEquals("Gemini rejected 1", liveVerificationSummary(listOf(Verdict.REJECTED)))
    }
}
