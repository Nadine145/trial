package com.example.playlistmanager

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * ════════════════════════════════════════════════════════════════════════════
 * Detailed View Activity - Shows all playlist items and calculates averages
 *
 * This screen displays:
 * - All songs with their details (using loops)
 * - Average rating calculation (using loops)
 * - Back navigation to main screen
 * ════════════════════════════════════════════════════════════════════════════
 */
class DetailedViewsActivity : AppCompatActivity() {

    private val TAG = "DetailedViewsActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_views)

        Log.d(TAG, "DetailedViewActivity created - Detailed view screen loaded")

        // Hide status bar for immersive experience
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        // ═══════════════════════════════════════════════════════════════════
        // Initialize views from XML layout
        // ═══════════════════════════════════════════════════════════════════
        val tvDisplay: TextView = findViewById(R.id.tvDisplay)
        val scrollView: ScrollView = findViewById(R.id.scrollView)
        val btnShowSongs: Button = findViewById(R.id.btnShowSongs)
        val btnCalculateAverage: Button = findViewById(R.id.btnCalculateAverage)
        val btnBack: Button = findViewById(R.id.btnBack)

        // ═══════════════════════════════════════════════════════════════════
        // Q.1.3 REQUIREMENT: BUTTON 1 - Display all songs using a loop (10 marks)
        // ═══════════════════════════════════════════════════════════════════
        btnShowSongs.setOnClickListener {
            Log.d(TAG, "Show Songs button clicked")
            animateButtonPress(it)
            displayAllItems(tvDisplay, scrollView)
        }

        // ═══════════════════════════════════════════════════════════════════
        // Q.1.3 REQUIREMENT: BUTTON 2 - Calculate average rating using a loop (10 marks)
        // ═══════════════════════════════════════════════════════════════════
        btnCalculateAverage.setOnClickListener {
            Log.d(TAG, "Calculate Average button clicked")
            animateButtonPress(it)
            calculateAndDisplayAverage(tvDisplay, scrollView)
        }

        // ═══════════════════════════════════════════════════════════════════
        // Q.1.3 REQUIREMENT: BUTTON 3 - Return to main screen (5 marks)
        // ═══════════════════════════════════════════════════════════════════
        btnBack.setOnClickListener {
            Log.d(TAG, "Back button clicked - Returning to MainActivity")
            animateButtonPress(it)
            it.postDelayed({
                finish()  // Close this activity and return to MainActivity
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            }, 200)
        }
    }

    /**
     * Animation for button press
     */
    private fun animateButtonPress(view: View) {
        view.animate()
            .scaleX(0.9f)
            .scaleY(0.9f)
            .setDuration(100)
            .withEndAction {
                view.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(100)
                    .start()
            }
            .start()
    }

    /**
     * ════════════════════════════════════════════════════════════════════════
     * Q.1.3 REQUIREMENT: DISPLAY ALL SONGS USING A LOOP (10 marks)
     *
     * This function displays all songs in the playlist with their details
     * It uses a FOR LOOP to iterate through the parallel arrays
     * ════════════════════════════════════════════════════════════════════════
     */
    private fun displayAllItems(textView: TextView, scrollView: ScrollView) {
        // Check if playlist is empty
        if (MainActivity.itemCount == 0) {
            Log.i(TAG, "No items to display")
            textView.text = "No songs in playlist yet!\n\nAdd some songs from the main screen."
            return
        }

        val displayText = StringBuilder()

        // Header
        displayText.append("╔════════════════════════════╗\n")
        displayText.append("   🎵  YOUR PLAYLIST  🎵\n")  // ← CHANGEABLE
        displayText.append("╚════════════════════════════╝\n\n")

        // ═══════════════════════════════════════════════════════════════════
        // LOOP THROUGH ALL ITEMS (Required for Q.1.3 - 10 marks)
        // This loop iterates through the parallel arrays and displays each song
        // ═══════════════════════════════════════════════════════════════════
        for (i in 0 until MainActivity.itemCount) {
            Log.d(TAG, "Displaying item $i: ${MainActivity.itemTitles[i]}")

            // Get star rating visualization
            val stars = getStarRating(MainActivity.itemRatings[i] ?: 0)

            // Display song details
            displayText.append("┌─────────────────────────────┐\n")
            displayText.append("│ SONG ${i + 1}\n")  // ← CHANGEABLE: "SONG" to "MOVIE", "BOOK", etc.
            displayText.append("├─────────────────────────────┤\n")
            displayText.append("│\n")

            // ═══ CHANGEABLE: Labels for different app types ═══
            displayText.append("│ 🎵 Title: ${MainActivity.itemTitles[i]}\n")
            displayText.append("│ 🎤 Artist: ${MainActivity.itemCreators[i]}\n")
            displayText.append("│ ⭐ Rating: $stars ${MainActivity.itemRatings[i]}/5\n")
            displayText.append("│ 💭 Comments: ${MainActivity.itemComments[i]}\n")
            displayText.append("│\n")
            displayText.append("└─────────────────────────────┘\n\n")
        }

        // Footer
        displayText.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n")
        displayText.append("   Total Songs: ${MainActivity.itemCount}/${MainActivity.MAX_ITEMS}\n")
        displayText.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━")

        // Display the text with animation
        animateTextChange(textView, displayText.toString())

        // Scroll to top
        scrollView.post {
            scrollView.smoothScrollTo(0, 0)
        }

        Log.i(TAG, "Successfully displayed all ${MainActivity.itemCount} items using a loop")
    }

    /**
     * ════════════════════════════════════════════════════════════════════════
     * Q.1.3 REQUIREMENT: CALCULATE AVERAGE RATING USING A LOOP (10 marks)
     *
     * This function calculates the average rating of all songs
     * It uses a FOR LOOP to sum all ratings, then divides by count
     * ════════════════════════════════════════════════════════════════════════
     */
    private fun calculateAndDisplayAverage(textView: TextView, scrollView: ScrollView) {
        // Check if playlist is empty
        if (MainActivity.itemCount == 0) {
            Log.i(TAG, "No items to calculate average")
            textView.text = "No songs to calculate average!\n\nAdd some songs first."
            return
        }

        // ═══════════════════════════════════════════════════════════════════
        // CALCULATE SUM USING A LOOP (Required for Q.1.3 - 10 marks)
        // This loop iterates through all ratings and calculates the total
        // ═══════════════════════════════════════════════════════════════════
        var totalRating = 0

        for (i in 0 until MainActivity.itemCount) {
            val rating = MainActivity.itemRatings[i] ?: 0
            totalRating += rating
            Log.d(TAG, "Loop iteration $i: Adding rating $rating (Running total: $totalRating)")
        }

        // Calculate average
        val averageRating = totalRating.toDouble() / MainActivity.itemCount

        Log.i(TAG, "Average rating calculated using loop: $averageRating (Total: $totalRating, Count: ${MainActivity.itemCount})")

        // Build display text
        val displayText = StringBuilder()

        // Header
        displayText.append("╔════════════════════════════╗\n")
        displayText.append("   📊  STATISTICS  📊\n")
        displayText.append("╚════════════════════════════╝\n\n")

        // Individual ratings section
        displayText.append("┌─────────────────────────────┐\n")
        displayText.append("│ INDIVIDUAL RATINGS\n")
        displayText.append("├─────────────────────────────┤\n")

        // Display each song's rating
        for (i in 0 until MainActivity.itemCount) {
            val rating = MainActivity.itemRatings[i] ?: 0
            val stars = getStarRating(rating)
            displayText.append("│ ${i + 1}. ${MainActivity.itemTitles[i]}\n")
            displayText.append("│    $stars $rating/5\n")
            if (i < MainActivity.itemCount - 1) {
                displayText.append("│\n")
            }
        }

        displayText.append("└─────────────────────────────┘\n\n")

        // Calculation section
        displayText.append("┌─────────────────────────────┐\n")
        displayText.append("│ CALCULATION\n")
        displayText.append("├─────────────────────────────┤\n")
        displayText.append("│ Total Songs: ${MainActivity.itemCount}\n")
        displayText.append("│ Sum of Ratings: $totalRating\n")
        displayText.append("│ Formula: $totalRating ÷ ${MainActivity.itemCount}\n")
        displayText.append("└─────────────────────────────┘\n\n")

        // Result section
        val stars = getStarRating(averageRating.toInt())
        val interpretation = getInterpretation(averageRating)

        displayText.append("╔═════════════════════════════╗\n")
        displayText.append("║  AVERAGE RATING\n")
        displayText.append("║\n")
        displayText.append("║  $stars\n")
        displayText.append("║  %.2f / 5.00\n".format(averageRating))
        displayText.append("║\n")
        displayText.append("║  $interpretation\n")
        displayText.append("╚═════════════════════════════╝")

        // Display the text with animation
        animateTextChange(textView, displayText.toString())

        // Scroll to top
        scrollView.post {
            scrollView.smoothScrollTo(0, 0)
        }

        Log.i(TAG, "Average rating display completed")
    }

    /**
     * Get star rating visualization
     * Converts numeric rating (1-5) to stars (★☆)
     */
    private fun getStarRating(rating: Int): String {
        val fullStars = "★".repeat(rating)
        val emptyStars = "☆".repeat(5 - rating)
        return fullStars + emptyStars
    }

    /**
     * Get interpretation of average rating
     */
    private fun getInterpretation(average: Double): String {
        return when {
            average >= 4.5 -> "🔥 MASTERPIECE COLLECTION!"
            average >= 4.0 -> "✨ EXCELLENT PLAYLIST!"
            average >= 3.5 -> "👍 GREAT SELECTION!"
            average >= 3.0 -> "😊 GOOD VIBES!"
            average >= 2.5 -> "🎵 DECENT MIX"
            average >= 2.0 -> "📝 ROOM TO IMPROVE"
            else -> "🎧 UNIQUE TASTE"
        }
    }

    /**
     * Animate text change with fade effect
     */
    private fun animateTextChange(textView: TextView, newText: String) {
        textView.animate()
            .alpha(0f)
            .setDuration(200)
            .withEndAction {
                textView.text = newText
                textView.animate()
                    .alpha(1f)
                    .setDuration(300)
                    .start()
            }
            .start()
    }
}

/**
 * ════════════════════════════════════════════════════════════════════════════
 * ASSIGNMENT REQUIREMENTS CHECKLIST:
 * ════════════════════════════════════════════════════════════════════════════
 *
 * Q.1.3 - Detailed View Screen (25 marks):
 * ✅ Button 1: Display songs using a loop (10 marks)
 *    - Loop iterates through all parallel arrays
 *    - Displays all song details (title, artist, rating, comments)
 *    - Shows corresponding details for each song
 *
 * ✅ Button 2: Calculate average rating using a loop (10 marks)
 *    - Loop sums all ratings from array
 *    - Calculates average (sum ÷ count)
 *    - Displays result to user
 *
 * ✅ Button 3: Return to main screen (5 marks)
 *    - Closes DetailedViewActivity
 *    - Returns to MainActivity
 *
 * ════════════════════════════════════════════════════════════════════════════
 *
 * CHANGEABLE FOR OTHER APPS:
 * - Line 52: "YOUR PLAYLIST" → "YOUR MOVIES", "YOUR BOOKS", etc.
 * - Line 67: "SONG" → "MOVIE", "BOOK", "RESTAURANT", etc.
 * - Line 72-75: Labels (Title, Artist, Rating, Comments)
 * - Line 81: "Total Songs" → "Total Movies", "Total Books", etc.
 *
 * ════════════════════════════════════════════════════════════════════════════
 */