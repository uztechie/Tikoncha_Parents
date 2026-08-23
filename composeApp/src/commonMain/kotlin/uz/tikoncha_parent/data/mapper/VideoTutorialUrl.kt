package uz.tikoncha_parent.data.mapper

import uz.tikoncha_parent.data.remote.model.tutorial.VideoTutorialData
import uz.tikoncha_parent.domain.model.TutorialUrls

fun VideoTutorialData.toTutorialUrls(): TutorialUrls = TutorialUrls(
    tikoncha = tikoncha_tutorial_url,
    policy = policy_tutorial_url,
    bindChild = bind_child_tutorial_url,
)