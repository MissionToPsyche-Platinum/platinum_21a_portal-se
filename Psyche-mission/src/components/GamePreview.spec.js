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
})
