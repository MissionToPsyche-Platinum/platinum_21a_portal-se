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

  it('unsaves the game when the heart is clicked again', async () => {
    const wrapper = mount(GameLink, {
      props: { game, textColor: '#ffffff' },
      ...routerStub
    })

    await wrapper.get('.favorite-btn').trigger('click')
    expect(JSON.parse(localStorage.getItem('favoriteGames'))).toContain('jolts-journey')

    await wrapper.get('.favorite-btn').trigger('click')

    expect(wrapper.vm.isFavorite).toBe(false)
    expect(JSON.parse(localStorage.getItem('favoriteGames'))).not.toContain('jolts-journey')
    expect(wrapper.get('.favorite-btn').attributes('aria-label')).toBe('Save to favorites')
  })

  it('falls back to the Psyche logo when the thumbnail is missing', () => {
    const wrapper = mount(GameLink, {
      props: {
        game: { ...game, thumbnail: 'None' }
      },
      ...routerStub
    })

    expect(wrapper.get('img.media').attributes('src')).not.toBe('None')
    expect(wrapper.get('img.media').attributes('src')).toBeTruthy()
  })

  it('uses the compact card style when compact is set', () => {
    const wrapper = mount(GameLink, {
      props: { game, compact: true },
      ...routerStub
    })

    expect(wrapper.get('.game-link-card').classes()).toContain('compact-card')
  })
})


