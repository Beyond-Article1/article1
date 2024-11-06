package article1be.outfit.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "outfit_style")
@ToString
public class OutfitStyle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long selectStyleSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "outfit_seq", nullable = false)
    private Outfit outfit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "style_seq", nullable = false)
    private Style style;

    public OutfitStyle(Outfit outfit, Style style) {
        this.outfit = outfit;
        this.style = style;
    }
}
