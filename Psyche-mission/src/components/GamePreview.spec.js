import { describe, expect, it } from 'vitest'
import { mount } from '@vue/test-utils'
import GamePreview from './GamePreview.vue'

const baseGame = {
  title: 'JOLTS JOURNEY',
  genre: 'Adventure',
  age: 'High School',
  difficulty: 'Hard',
  description: 'Travel to the Psyche asteroid.',
  video: ''
}

describe('GamePreview', () => {
  it('plays a gif in the hover preview', () => {
    const wrapper = mount(GamePreview, {
      props: {
        game: { ...baseGame, video: '/gif/jolts_journey.gif' }
      }
    })

    const gif = wrapper.get('img.preview-video')
    expect(gif.attributes('src')).toContain('gif/jolts_journey.gif')
    expect(wrapper.find('.preview-placeholder').exists()).toBe(false)
  })

  it('shows a placeholder when the game has no preview media', () => {
    const wrapper = mount(GamePreview, {
      props: { game: baseGame }
    })

    expect(wrapper.get('.preview-placeholder').text()).toContain('No preview available')
    expect(wrapper.find('img.preview-video').exists()).toBe(false)
    expect(wrapper.find('iframe').exists()).toBe(false)
  })

  it('shows Hard difficulty with a filled three-segment bar', () => {
    const wrapper = mount(GamePreview, {
      props: { game: baseGame }
    })

    expect(wrapper.get('.difficulty-label').text()).toContain('Hard')
    expect(wrapper.vm.difficultyLevel).toBe(3)
  })

  it('embeds a YouTube preview when the video URL is YouTube', () => {
    const wrapper = mount(GamePreview, {
      props: {
        game: {
          ...baseGame,
          video: 'https://www.youtube.com/embed/r60lcfc1aAA'
        }
      }
    })

    expect(wrapper.get('iframe.preview-iframe').attributes('src')).toContain('youtube.com')
    expect(wrapper.find('img.preview-video').exists()).toBe(false)
  })

  it('treats Easy difficulty as one filled bar segment', () => {
    const wrapper = mount(GamePreview, {
      props: {
        game: { ...baseGame, difficulty: 'Easy' }
      }
    })

    expect(wrapper.get('.difficulty-label').text()).toContain('Easy')
    expect(wrapper.vm.difficultyLevel).toBe(1)
  })
})



