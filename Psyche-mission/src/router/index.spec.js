import { describe, expect, it } from 'vitest'
import router from './index'

describe('router', () => {
  it('defines the main portal routes', () => {
    const paths = router.getRoutes().map((route) => route.path)

    expect(paths).toContain('/')
    expect(paths).toContain('/login')
    expect(paths).toContain('/game/:id')
    expect(paths).toContain('/vr-instructions')
  })

  it('names the game and 404 routes', () => {
    const names = router.getRoutes().map((route) => route.name)

    expect(names).toContain('GamePage')
    expect(names).toContain('NotFound')
    expect(names).toContain('Login')
  })
})
