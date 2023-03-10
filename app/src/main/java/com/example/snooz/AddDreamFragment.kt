package com.example.snooz

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.aallam.openai.api.ExperimentalOpenAI
import com.aallam.openai.api.edits.EditsRequest
import com.aallam.openai.api.image.ImageCreationURL
import com.aallam.openai.api.image.ImageSize
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.example.snooz.databinding.FragmentAddDreamBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import java.util.*

//Without id, Dream class is with id tag cause getting id from firebase
class DreamData{
    var name = ""
    var tag = ""
    var text = ""
    var date = ""
    constructor(name: String, tag: String, text: String, date: String){
        this.name = name
        this.tag= tag
        this.text = text
        this.date = date
    }
}

class AddDreamFragment : Fragment() {
    private var _binding: FragmentAddDreamBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddDreamBinding.inflate(inflater, container, false)
        val view = binding.root

        val openAI = OpenAI(BuildConfig.API_KEY)

        database = Firebase.database.reference

        //Getting the current date in DD/MM/YY format so I can add it to the firebase dreams call
        val dateTime = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val formattedDateTime = formatter.format(dateTime)

        //Get users current id
        val storedData = activity?.getSharedPreferences("sharedPreferences", AppCompatActivity.MODE_PRIVATE)
        val sharedPreferencesId = storedData?.getString("id", "")

        //once the user clicks the button, push the data to firebase
        _binding!!.addDreamButton.setOnClickListener {
            if(binding.dreamNameInput.text.isEmpty() || binding.dreamTextInput.text.isEmpty() || binding.dreamTagInput.text.isEmpty()){
                Snackbar.make(binding.addDreamLayout, "There are some empty fields", Snackbar.LENGTH_LONG).setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.dark_gray)).setTextColor(ContextCompat.getColor(
                    requireActivity(), R.color.white)).show()

            }else {
                val currentDreamData = DreamData(binding.dreamNameInput.text.toString(), binding.dreamTagInput.text.toString(), binding.dreamTextInput.text.toString(), formattedDateTime)
                database.child("/${sharedPreferencesId}/dreams").push().setValue(currentDreamData)

                Snackbar.make(binding.addDreamLayout, "Dream Added Successfully", Snackbar.LENGTH_LONG).setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.dark_gray)).setTextColor(ContextCompat.getColor(
                    requireActivity(), R.color.white)).show()

                binding.dreamNameInput.text.clear()
                binding.dreamTagInput.text.clear()
                binding.dreamTextInput.text.clear()
                binding.dreamImage.visibility = View.GONE
            }
        }

        _binding!!.autocorrectDreamButton.setOnClickListener{
            lifecycleScope.launch{
                autocorrectDream(openAI, binding.dreamTextInput.text.toString())
            }
        }

        _binding!!.dreamImageButton.setOnClickListener{
            lifecycleScope.launch {
                generateDreamImage(openAI, binding.dreamTextInput.text.toString())
            }
        }
        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private suspend fun autocorrectDream(openAI: OpenAI, dream: String){
        val autocorrectResult = openAI.edit(
            request = EditsRequest(
                model = ModelId("text-davinci-edit-001"),
                input = dream,
                instruction = "Fix the spelling and grammar mistakes"
            )
        )
        binding.dreamTextInput.setText(autocorrectResult.choices[0].text)
    }

    @OptIn(ExperimentalOpenAI::class)
//    in realistic version? use tag instead?
    private suspend fun generateDreamImage(openAI: OpenAI, dream: String){
        val images = openAI.image( // or openAI.imageJSON
            creation = ImageCreationURL(
                prompt = "Create a dreamy high quality rendition of this prompt - ${dream}",
                n = 2,
                size = ImageSize.is1024x1024
            )
        )

        Picasso.get().load(images[0].url).into(binding.dreamImage);
        binding.dreamImage.visibility = View.VISIBLE
    }
}