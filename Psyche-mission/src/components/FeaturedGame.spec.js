import { describe, expect, it } from 'vitest'
import { mount } from '@vue/test-utils'
import FeaturedGame from './FeaturedGame.vue'

const game = {
  id: 'jolts-journey',
  title: 'JOLTS JOURNEY',
  thumbnail: 'https://example.com/jolt.png',
  description: 'Travel to the Psyche asteroid.',
  difficulty: 'Hard',
  genre: 'Adventure',
  age: 'High School',
  class: 'Nickel - 2020-2021',
  credits: 'Team Nickel'
}

describe('FeaturedGame', () => {
  it('shows featured title, class year, and a Play link', () => {
    const wrapper = mount(FeaturedGame, {
      props: { game },
      global: {
        stubs: {
          'router-link': {
            props: ['to'],
            template: '<a :href="to && to.name ? to.name : \'/\'"><slot /></a>'
          }
        }
      }
    })

    expect(wrapper.get('.hero-title').text()).toBe('JOLTS JOURNEY')
    expect(wrapper.text()).toContain('Nickel - 2020-2021')
    expect(wrapper.text()).toContain('Play')
  })

  it('falls back to the Psyche logo when the thumbnail is missing', () => {
    const wrapper = mount(FeaturedGame, {
      props: {
        game: { ...game, thumbnail: 'None' }
      },
      global: {
        stubs: {
          'router-link': true
        }
      }
    })

    expect(wrapper.vm.gameImage).not.toBe('None')
    expect(wrapper.vm.gameImage).toBeTruthy()
  })
})
