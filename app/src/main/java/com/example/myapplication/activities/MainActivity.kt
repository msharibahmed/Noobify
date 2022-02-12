package com.example.myapplication.activities

import android.media.MediaPlayer
import android.os.Bundle
import android.view.Window
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var mainActivityBinding: ActivityMainBinding
    private lateinit var layout: LinearLayoutManager
    private lateinit var paginationLoadingProgressBar: ProgressBar
    //  private var playlistAdapter: PlaylistAdapter = PlaylistAdapter(this@MainActivity, this)

    var showMiniPlayerCheck: Boolean = false //using to not show miniPlayer during screen loading


    private var playPause = false //help to toggle between play and pause.

    private var initialStage =
        true // remain false till media is not completed, inside OnCompletionListener make it true.
    var mediaPlayer: MediaPlayer? = null
    var currentSong: String? = null
    var isScrolling: Boolean = false
    var currentSongIndex: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_MyApplication)
        mainActivityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainActivityBinding.root)
        val window :Window = this@MainActivity.window
        window.statusBarColor= ContextCompat.getColor(this,R.color.teal_700)
    }
/*
    override fun onItemClickChangeMiniPlayer(
        playlistItemModel: PlaylistItemModel.Short,
        index: Int
    ) {
        TODO("Not yet implemented")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
          super.onCreate(savedInstanceState)
          initializeLayout()
          initializingMediaPlayer()
          paginationLoadingProgressBar = mainActivityBinding.progressBar
          mainActivityBinding.miniPlayerPlayBtn.setOnClickListener(pausePlay)
          mainActivityBinding.miniPlayerNextBtn.setOnClickListener(nextPlay)
          mainActivityBinding.miniPlayerPrevBtn.setOnClickListener(prevPlay)
          setUpRecyclerView()
          loadPlaylistItems()
          setUpRecycleViewScrollListener()


      }


      private fun initializeLayout() {

          setTheme(R.style.Theme_MyApplication)
          mainActivityBinding = ActivityMainBinding.inflate(layoutInflater)
          setContentView(mainActivityBinding.root)
          layout = LinearLayoutManager(this@MainActivity)
      }

      private fun initializingMediaPlayer() {
          mediaPlayer = MediaPlayer()
          mediaPlayer!!.setAudioAttributes(
              AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build()
          )
      }

      private fun setUpRecyclerView() {

          mainActivityBinding.listSongsPlaylist.apply {
              setHasFixedSize(true)
              setItemViewCacheSize(10)
              layoutManager = layout
              adapter = playlistAdapter
          }

      }

      private fun setUpRecycleViewScrollListener() {
          // using scroll listener to notify when list is finished and call api
          mainActivityBinding.listSongsPlaylist.addOnScrollListener(object :
              RecyclerView.OnScrollListener() {
              override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                  super.onScrollStateChanged(recyclerView, newState)
                  isScrolling = true
                  // Log.d("isScrolling $isScrolling", "during scrolling")
              }

              override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                  super.onScrolled(recyclerView, dx, dy)

                  val totalItems = playlistAdapter.itemCount //total items in the list
                  val currentItems = layout.childCount  // current visible items on the screen
                  val scrolledOutItems = layout.findFirstVisibleItemPosition() // scrolled out items

                  if (isScrolling && (currentItems + scrolledOutItems >= totalItems)) {
                      isScrolling = false
                      // Log.d("isScrolling $isScrolling", "after Scrolled")
                      loadPlaylistItems() //calling api again
                  }

              }
          })

      }


      private fun loadPlaylistItems() {
          paginationLoadingProgressBar.visibility =  View.VISIBLE //using this progress bar during api call

          //initiate the service
          val destinationService = ServiceBuilder.buildService(PlaylistItemService::class.java)
          val requestCall = destinationService.getSongDetail()

          //make network call asynchronously
          requestCall.enqueue(object : Callback<PlaylistItemModel> {
              @SuppressLint("SetTextI18n")
              override fun onResponse(
                  call: Call<PlaylistItemModel>,
                  response: Response<PlaylistItemModel>
              ) {
                  if (paginationLoadingProgressBar.isVisible) {
                      paginationLoadingProgressBar.visibility = View.GONE
                  }
                  Log.d("Response", "onResponse: ${response.body()}")
                  if (response.isSuccessful) {
                      val playlistItemList = response.body()!!
                      // Log.d("Response", "playlist size : ${playlistItemList.shorts.size}")

                      //adding data to the list using add() method in PlaylistAdapter class
                      playlistAdapter.add(playlistItemList)

                      if (!showMiniPlayerCheck && playlistItemList.shorts.isNotEmpty()) {
                          currentSongIndex = 0
                          mainActivityBinding.apply {
                              miniPlayerSongName.text =
                                  playlistItemList.shorts[currentSongIndex!!].title
                              miniPlayerCreatorName.text =
                                  "Anonymous" + playlistItemList.shorts[currentSongIndex!!].creator.userID
                              miniPlayerPrevBtn.isEnabled = currentSongIndex != 0
                              miniPlayerPrevBtn.setColorFilter(resources.getColor(R.color.white))
                          }

                          currentSong = playlistItemList.shorts[currentSongIndex!!].audioPath

                          showMiniPlayer()
                          showMiniPlayerCheck = true
                      }


                  } else {
                      Toast.makeText(
                          this@MainActivity,
                          "Something went wrong ${response.message()}",
                          Toast.LENGTH_SHORT
                      ).show()
                  }
              }

              override fun onFailure(call: Call<PlaylistItemModel>, t: Throwable) {
                  if (paginationLoadingProgressBar.isVisible) { //hiding progressbar if failed to call api
                      paginationLoadingProgressBar.visibility = View.GONE
                  }

                  Toast.makeText(this@MainActivity, "Something went wrong $t", Toast.LENGTH_SHORT)
                      .show()
              }
          })
      }

      private fun showMiniPlayer() {

          mainActivityBinding.miniPlayerCard.visibility = View.VISIBLE

      }

      override fun onItemClickChangeMiniPlayer(
          playlistItemModel: PlaylistItemModel.Short,
          index: Int,
      ) {
          playSelectedSong(playlistItemModel, index)

      }

      @SuppressLint("SetTextI18n")
      private fun playSelectedSong(playlistItemModel: PlaylistItemModel.Short, index: Int) {
          mainActivityBinding.apply {
              miniPlayerSongName.text = playlistItemModel.title
              miniPlayerCreatorName.text = "Anonymous" + playlistItemModel.creator.userID

              if (index == 0) {
                  mainActivityBinding.miniPlayerPrevBtn.isEnabled = false
                  mainActivityBinding.miniPlayerPrevBtn.setColorFilter(resources.getColor(R.color.white))
              } else {

                  mainActivityBinding.miniPlayerPrevBtn.isEnabled = true
                  mainActivityBinding.miniPlayerPrevBtn.setColorFilter(resources.getColor(R.color.black))
              }
          }
          currentSong = playlistItemModel.audioPath
          currentSongIndex = index
          Log.d("nextSongINDEX", "currentIndex->$currentSongIndex")
          uninitializeMediaPLayer()
          songOnClickCallBack()
      }

      private fun uninitializeMediaPLayer() {
          initialStage = true
          playPause = false
          mainActivityBinding.miniPlayerPlayBtn.setImageResource(R.drawable.play_btn_icon)
          mediaPlayer!!.stop()
          mediaPlayer!!.reset()
      }

      private val pausePlay: View.OnClickListener =
          View.OnClickListener {
              songOnClickCallBack()
          }

      private fun playNextSongAutomatically() {
          playNextSongLogic()
      }

      private fun playNextSongLogic() {

          val nextSongIdx = (currentSongIndex?.plus(1))?.rem(playlistAdapter.itemCount)
          Log.d("nextSongINDEX", "currentIndex->$currentSongIndex, nextIndex->$nextSongIdx")
          val playlistItem: PlaylistItemModel.Short =
              playlistAdapter.getNextItem(nextSongIdx!!)
          playSelectedSong(playlistItem, nextSongIdx)
      }

      private val nextPlay: View.OnClickListener =
          View.OnClickListener {
              uninitializeMediaPLayer()
              playNextSongLogic()
          }
      private val prevPlay: View.OnClickListener =
          View.OnClickListener {
              if (currentSongIndex!! >= 0) {
                  uninitializeMediaPLayer()
                  val prevSongIdx = (currentSongIndex?.minus(1))?.rem(playlistAdapter.itemCount)
                  Log.d("prevINDEX", "currentIndex->$currentSongIndex, prevIndex->$prevSongIdx")
                  val playlistItem: PlaylistItemModel.Short =
                      playlistAdapter.getNextItem(prevSongIdx!!)
                  playSelectedSong(playlistItem, prevSongIdx)
              }
          }

      private fun songOnClickCallBack() {
          playPause = if (!playPause) {
              mainActivityBinding.miniPlayerPlayBtn.setImageResource(R.drawable.pause_btn_icon)
              if (initialStage) {
                  Player()
                      .execute(currentSong)
              } else {
                  if (!mediaPlayer!!.isPlaying) mediaPlayer!!.start()
              }
              true
          } else {
              mainActivityBinding.miniPlayerPlayBtn.setImageResource(R.drawable.play_btn_icon)
              if (mediaPlayer!!.isPlaying) mediaPlayer!!.pause()
              false
          }
      }

      // preparing mediaPlayer will take sometime to buffer the content so prepare it inside the background thread and starting it on UI thread.
      inner class Player : AsyncTask<String?, Void?, Boolean>() {
          override fun doInBackground(vararg params: String?): Boolean {
              var prepared: Boolean
              var mediaFile: File
              try {
                  val cn: URLConnection = URL(params[0]).openConnection()
                  val `is`: InputStream = cn.getInputStream()

                  // create file to store audio
                  mediaFile = File(cacheDir, "mediafile")
                  val fos = FileOutputStream(mediaFile)
                  val buf = ByteArray(16 * 1024)
                  Log.i("FileOutputStream", "Download")

                  // write to file until complete
                  do {
                      val numread: Int = `is`.read(buf)
                      if (numread <= 0) break
                      fos.write(buf, 0, numread)
                  } while (true)
                  fos.flush()
                  fos.close()
                  Log.i("FileOutputStream", "Saved")

                  mediaPlayer!!.setOnCompletionListener {
                      uninitializeMediaPLayer()
                      playNextSongAutomatically()
                  }
                  val fis = FileInputStream(mediaFile)

                  // set mediaplayer data source to file descriptor of input stream
                  mediaPlayer!!.setDataSource(fis.fd)
                  mediaPlayer!!.prepare()

                  prepared = true
              } catch (e: IllegalArgumentException) {
                  //playNextSongAutomatically()
                  Log.d("IllegalArgument", "Caught Exception")
                  e.message?.let { Log.d("IllegalArgument", it) }

                  prepared = false
                  e.printStackTrace()
              } catch (e: SecurityException) {
                  // playNextSongAutomatically()
                  Log.d("Security", "Caught Exception")

                  prepared = false
                  e.printStackTrace()
              } catch (e: IllegalStateException) {
                  //  playNextSongAutomatically()
                  Log.d("IllegalState", "Caught Exception")

                  prepared = false
                  e.printStackTrace()
              } catch (e: IOException) {
                  Log.d("IO", "Caught Exception")

                  prepared = false
                  e.printStackTrace()
              } finally {
                  Runnable { this@MainActivity.runOnUiThread { playNextSongAutomatically() } }
              }

              return prepared
          }

          override fun onPostExecute(result: Boolean) {
              super.onPostExecute(result)
              /* if (progress.isShowing) {
                   progress.cancel()
               }*/
              mainActivityBinding.audioLoadingProgress
                  .visibility = View.GONE

              mainActivityBinding.miniPlayerPlayBtn.visibility = View.VISIBLE
              Log.d("Prepared", "//$result")
              if (!result) {
                  showCustomToast("media error, try next song")
              }
              mediaPlayer?.start()
              initialStage = false
          }

          override fun onPreExecute() {
              super.onPreExecute()
              /*  progress.setMessage("Buffering...")
                progress.show()*/
              mainActivityBinding.miniPlayerPlayBtn.visibility = View.GONE
              mainActivityBinding.audioLoadingProgress.visibility = View.VISIBLE


          }


      }

      private fun showCustomToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
          Toast.makeText(this@MainActivity, "Something went wrong $message", duration)
              .show()
      }

     */


}


