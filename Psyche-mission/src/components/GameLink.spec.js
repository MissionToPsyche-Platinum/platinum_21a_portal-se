import { beforeEach, describe, expect, it } from 'vitest'
import { mount } from '@vue/test-utils'
import GameLink from './GameLink.vue'

const game = {
  id: 'jolts-journey',
  title: 'JOLTS JOURNEY',
  thumbnail: 'https://example.com/jolt.png'
}

const routerStub = {
  global: {
    stubs: {
      'router-link': {
        props: ['to'],
        template: '<a class="game-route"><slot /></a>'
      }
    }
  }
}

describe('GameLink', () => {
  beforeEach(() => {
    localStorage.clear()
  })

  it('shows the game title on the card', () => {
    const wrapper = mount(GameLink, {
      props: { game },
      ...routerStub
    })

    expect(wrapper.get('.game-title').text()).toBe('JOLTS JOURNEY')
    expect(wrapper.get('img.media').attributes('alt')).toBe('JOLTS JOURNEY')
  })

  it('saves the game as a favorite when the heart is clicked', async () => {
    const wrapper = mount(GameLink, {
      props: { game, textColor: '#ffffff' },
      ...routerStub
    })

    await wrapper.get('.favorite-btn').trigger('click')

    expect(wrapper.vm.isFavorite).toBe(true)
    expect(JSON.parse(localStorage.getItem('favoriteGames'))).toContain('jolts-journey')
  })
})
