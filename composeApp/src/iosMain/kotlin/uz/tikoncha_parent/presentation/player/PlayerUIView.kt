package uz.tikoncha_parent.presentation.player

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readValue
import kotlinx.cinterop.useContents
import platform.AVFoundation.AVLayerVideoGravityResizeAspect
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.AVPlayerLayer
import platform.CoreGraphics.CGRectZero
import platform.UIKit.UIView

@OptIn(ExperimentalForeignApi::class)
class PlayerUIView(player: AVPlayer) : UIView(frame = CGRectZero.readValue()) {

    val playerLayer: AVPlayerLayer = AVPlayerLayer.playerLayerWithPlayer(player).apply {
        videoGravity = AVLayerVideoGravityResizeAspect
    }

    init {
        layer.addSublayer(playerLayer)
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
        playerLayer.setFrame(bounds)
    }
}