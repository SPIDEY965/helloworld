<template>
    <div class="wrap">
        <UserTitleComp />
        <div class="right-body">
            <div class="story" id="story">
                <div class="select-story">
                    <splide :options="options" class="slider">
                        <splide-slide class="splide-slide" v-for="stories in this.story" :key="stories">
                            <div class="one-slide" v-for="oneStory in stories" :key="oneStory">
                                <div :class="`story-element-container-${oneStory.storySeq} isRead_${oneStory.isRead} story-element-container`"
                                    @click="showStoryInfo" :id="`${oneStory.storySeq}`">
                                    <img class="story-element" src="@/assets/KakaoTalk_20230116_110321475_05.jpg" alt="스토리"
                                        v-if="oneStory.imgUrl == '' || oneStory.imgUrl == null" :id="`${oneStory.storySeq}`" />
                                    <img class="story-element" :src="`${oneStory.imgUrl}`" alt="스토리"
                                        v-if="oneStory.imgUrl != '' && oneStory.imgUrl != null" :id="`${oneStory.storySeq}`" />
                                </div>
                                <!-- <div class="story-element-container" v-for="i in (10 - stories.length)" :key="i">
                                    <img class="story-element" src="@/assets/KakaoTalk_20230116_110321475_05.jpg" alt="스토리"
                                        hidden />
                                </div> -->
                            </div>
                            <div class="one-slide" v-for="i in (10 - stories.length)" :key="i" hidden>
                            </div>
                        </splide-slide>
                    </splide>
                    <!-- <div v-else style="line-height: 15vh">
                        조회할 스토리가 없습니다
                    </div> -->
                </div>
            </div>

            <div class="notice">{{ this.masterNickname }} 님의 방</div>
            <div class="room-container">
                <img class="room" src="@/assets/image/room/room1.png" v-if="this.roomNum == 1" />
                <img class="room" src="@/assets/image/room/room2.png" v-if="this.roomNum == 2" />
                <img class="room" src="@/assets/image/room/room3.png" v-if="this.roomNum == 3" />
                <img class="room" src="@/assets/image/room/room4.png" v-if="this.roomNum == 4" />
                <div class="avatar-container">
                    <img class="avatar" :src="`${this.avatarUrl}`" />
                </div>
            </div>

            <div class="notice">{{ this.masterNickname }} 님 일촌들의 한 마디</div>
            <div class="family-comment-container">
                <FamilyComment />
            </div>
            <div class="notice">{{ this.masterNickname }} 님이 심은 잔디</div>
            <div class="grass-container">
                <GrassComp />
            </div>
        </div>
    </div>
    <Teleport to="body">
        <modal :show="showModal" @close="showModal = false" :title="`${this.title}`" :author="`${this.author}`"
            :imgUrl="`${this.imgUrl}`" :boardSeq="`${this.boardSeq}`" :authorSeq="`${this.authorSeq}`">
            <template #header>
                <h3>custom header</h3>
            </template>
        </modal>
    </Teleport>
</template>

<script>
import UserTitleComp from "@/components/BasicComp/UserTitleComp.vue"
import GrassComp from "@/components/MainPageComp/GrassComp.vue"
import FamilyComment from "@/components/MainPageComp/FamilyComment.vue"
import http from '@/api/httpWithAccessToken';
import httpStory from '@/api/httpStory';
import Modal from '@/components/MainPageComp/StoryModal.vue'

export default {
    components: { UserTitleComp, GrassComp, FamilyComment, Modal },
    data() {
        return {
            options: {
                type: 'slide',
                rewind: false,
                perPage: 1,
                autoplay: false,
                pauseOnHover: false,
                arrows: false,
                dots: false,
                animatedDots: false,
                speed: 800,
                fixedWidth: '100%',
                padding: 10,
                pagination: false,
            },
            avatarUrl: null,
            readStory: [],
            newStory: [],
            story: [],
            masterSeq: this.$route.params.userSeq,
            userSeq: localStorage.getItem('user-seq'),
            showModal: false,
            title: null,
            author: null,
            authorSeq: null,
            boardSeq: null,
            imgUrl: null,
            roomNum: 1,
            masterNickname: null,
        };
    },
    methods: {
        showStoryInfo: async function (e) {
            e.preventDefault();
            // console.log(e.target.id)
            console.log(`story-element-container-${e.target.id}`)
            var temp = e.target.closest('div');
            if (temp.classList.contains('isRead_0')) {
                temp.classList.remove(`isRead_0`);
                temp.classList.add(`isRead_1`);
            }
            console.log(document.getElementsByClassName(`story-element-container-${e.target.id}`));
            httpStory.get(`/story/${e.target.id}`).then((result) => {
                console.log(result.data)
                this.title = result.data.title;
                this.author = result.data.nickname;
                this.boardSeq = result.data.boardSeq.toString();
                this.authorSeq = result.data.writerSeq.toString();
                this.imgUrl = result.data.imgUrl;
                this.showModal = true;
            }, (error) => {
                console.log(error)
            })

        }
    },
    async created() {
        // var userSeq = localStorage.getItem('user-seq');
        await http.get(`/user/mainpage/${this.masterSeq}`).then((result) => {
            // this.avatarUrl = result.data.data.avatarUrl;
            console.log(result.data.data)
            this.masterNickname = result.data.data.nickname;
            this.avatarUrl = result.data.data.avatarUrl;
            // this.roomNum = 4
            if (result.data.data.likeCnt + result.data.data.understandCnt + result.data.data.helpfulCnt > 10 || result.data.data.today > 10) {
                this.roomNum = 2;
            }
            if (result.data.data.likeCnt + result.data.data.understandCnt + result.data.data.helpfulCnt > 100 || result.data.data.today > 100) {
                this.roomNum = 3;
            }
            if (result.data.data.likeCnt + result.data.data.understandCnt + result.data.data.helpfulCnt > 500 || result.data.data.today > 500) {
                this.roomNum = 4;
            }
        }, (error) => {
            console.log(error);
        });
        await httpStory.get(`/story/all/${this.userSeq}`).then((result) => {
            this.readStory = result.data.readStory;
            this.newStory = result.data.newStory;
            var temp = []
            for (var i = 0; i < this.readStory.length + this.newStory.length; i++) {
                if (i % 10 == 0 && i != 0) {
                    this.story.push(temp);
                    temp = []
                }
                else {
                    if (i < this.newStory.length) {
                        temp.push(this.newStory[i])
                    }
                    else {
                        temp.push(this.readStory[i - this.newStory.length])
                    }
                }
            }
            this.story.push(temp);
            console.log(result.data);
            if(this.story[0].length == 0) {
                document.querySelector('#story').style.display = 'none';
            }
        }, (error) => {
            console.log(error);
        })
    }
}

</script>

<style scoped>
.notice {
    text-align: start;
    margin-top: 2%;
    margin-left: 2.5%;
    color: #82ACC1;
    font-size: 14px;
    font-weight: 600;
}

.avatar-container {
    width: 15%;
    height: 30%;
    overflow: hidden;
    position: absolute;
    top: 66%;
    left: 42%;
}

.avatar {
    width: 100%;
    height: 100%;
    object-fit: fill;
}

.right-body {
    background-color: white;
    width: 62vw;
    height: 75vh;
    border: 1px solid #6A6A6A;
    border-radius: 15px;
    overflow: hidden;
    overflow-y: scroll;
}

.story {
    width: 98%;
    height: 13vh;
    margin: 0 auto;
    display: flex;
    justify-content: center;
}

.splide-slide {
    display: flex;
    height: 15vh;
}

.one-slide {
    display: flex;
    justify-content: center;
    margin-top: 3vh;
    width: 90%;
    overflow: hidden;
}

.story-element-container {
    width: 65px;
    height: 65px;
    border-radius: 100%;
    overflow: hidden;
}

.isRead_0 {
    border: 2px solid red;
}

.isRead_1 {
    border: 2px solid #D9D9D9;
    filter: grayscale(0.5);
}

.story-element {
    width: 100%;
    height: 100%;
    object-fit: fill;
    cursor: pointer;
}


.grass-container {
    width: 95%;
    height: 20%;
    border: 1px solid #F6F6F6;
    border-radius: 5px;
    margin: 0 auto;
    margin-top: 5px;
    background-color: #F7F7F7;
}

.grass {
    width: 98%;
    height: 100%;
    margin: 0 auto;
}

.select-story {
    width: 100%;
}

.family-comment-container {
    width: 95%;
    height: 20%;
    border-radius: 5px;
    margin: 0 auto;
    margin-top: 5px;
    background-color: #F0F0F0;
}

.room-container {
    width: 95%;
    height: 65%;
    border-radius: 10px;
    overflow: hidden;
    margin: 0 auto;
    margin-top: 5px;
    position: relative;
}

.room {
    width: 100%;
    height: 100%;
    object-fit: fill;
}

.comments::-webkit-scrollbar {
    width: 6px;
    /*스크롤바의 너비*/
}

.comments::-webkit-scrollbar-thumb {
    background-color: #A3A3A3;
    border-radius: 3px;
}

.comments::-webkit-scrollbar-track {
    background-color: white;
    border-radius: 3px;
    border: 1.5px solid #6A6A6A;

}

.right-body::-webkit-scrollbar {
    /* width: 6px; */
    display: none;
    /*스크롤바의 너비*/
}

/* 
.right-body::-webkit-scrollbar-thumb {
    background-color: #A3A3A3;
    border-radius: 3px;
}

.right-body::-webkit-scrollbar-track {
    background-color: white;
    border-radius: 3px;
    border: 1.5px solid #6A6A6A;

} */
</style>